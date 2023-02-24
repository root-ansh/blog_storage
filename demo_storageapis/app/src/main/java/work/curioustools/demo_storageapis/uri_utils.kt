package work.curioustools.demo_storageapis

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT

import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import org.json.JSONObject
import work.curioustools.demo_storageapis.ui.FileModel
import work.curioustools.demo_storageapis.ui.TypeUI
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*

fun Uri.toFileModel(): FileModel {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path) // uriAsFile.extension
    val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    return toFileModel(mimetype)
}

fun Uri.toFileModel(mimeType:String?): FileModel {

    val isFromContentFramework = scheme.equals(ContentResolver.SCHEME_CONTENT,true)
    val isFromLegacyFileFramework = scheme.equals(ContentResolver.SCHEME_FILE,true)
    val isWebUrl = scheme.equals("http",true) || scheme.equals("https",true)

    if(isFromLegacyFileFramework){

        val uriAsFile = this.toFile()
        val isADirectory = uriAsFile.isDirectory

        val name = uriAsFile.name // or uriAsFile.nameWithoutExtension // or simply this.lastPathSegment


        val size = if(isADirectory) "" else uriAsFile.length().toMemoryString()

        val typeUI = if(isADirectory) TypeUI.FOLDER else TypeUI.getTypeFromFileName(name)

        val internalItemsSize = if(!isADirectory) "" else (uriAsFile.list()?.size?:0).toString() //note, for directory, if android:requestLegacyExternalStorage=true is present in manifest, then only it will return file sizes otherwise it won't. also your app must have read storage permission

        val lastModified = Date(uriAsFile.lastModified())



        return FileModel(title = name, path = TypeUI.getEmojiPath(pathSegments,typeUI.emoji), fileSize = size, internalItemsSize , res = typeUI.icon,mimeType?:"?/?")

    }
    else {
        //todo
        return FileModel("not a file:// uri", path?:"", "", "", 0,"")
    }
}

fun Uri.toJson(mimetypeExtension:Context?=null,showFile:Boolean=false ): JSONObject {
    val res = JSONObject()

    runLogging { res.put("authority", this.authority) }

    runLogging { res.put("fragment", this.fragment) }
    runLogging { res.put("host", this.host) }


    runLogging { res.put("lastPathSegment", this.lastPathSegment) }
    runLogging { res.put("path", this.path) }
    runLogging { res.put("pathSegments", this.port) }
    runLogging { res.put("pathSegments", this.pathSegments) }
    runLogging { res.put("query", this.query) }
    runLogging { res.put("queryParameterNames", this.queryParameterNames) }
    runLogging { res.put("scheme", this.scheme) }
    runLogging { res.put("userInfo", this.userInfo) }


    val j = JSONObject().also {
        runLogging { it.put("encodedAuthority", this.encodedAuthority) }
        runLogging { it.put("encodedFragment", this.encodedFragment) }
        runLogging { it.put("encodedPath", this.encodedPath) }
        runLogging { it.put("encodedQuery", this.encodedQuery) }
        runLogging { it.put("encodedUserInfo", this.encodedUserInfo) }
        runLogging { it.put("encodedSchemeSpecificPart", this.encodedSchemeSpecificPart) }
    }
    res.put("encoding", j)

    val checks = JSONObject().also {
        runLogging { it.put("isAbsolute", this.isAbsolute) }
        runLogging { it.put("isHierarchical", this.isHierarchical) }
        runLogging { it.put("isOpaque", this.isOpaque) }
        runLogging { it.put("isRelative", this.isRelative) }

    }
    runLogging { res.put("checks", checks) }

    if(showFile) {
        runLogging {
            val file = this.toFile()
            res.put("toFile", file.toJson())
            if (SDK_INT >= Build.VERSION_CODES.O && file.exists()) {
                val attrs: BasicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
                val sdk26Attrs = JSONObject()
                runLogging { sdk26Attrs.put("fileKey", attrs.fileKey()) }
                runLogging { sdk26Attrs.put("isDirectory", attrs.isDirectory) }
                runLogging { sdk26Attrs.put("isOther", attrs.isOther) }
                runLogging { sdk26Attrs.put("isRegularFile", attrs.isRegularFile) }
                runLogging { sdk26Attrs.put("isSymbolicLink", attrs.isSymbolicLink) }
                runLogging { sdk26Attrs.put("creationTime", attrs.creationTime()?.toString()) }
                runLogging { sdk26Attrs.put("lastAccessTime", attrs.lastAccessTime()?.toString()) }
                runLogging { sdk26Attrs.put("lastModifiedTime", attrs.lastModifiedTime()?.toString()) }
                runLogging { sdk26Attrs.put("size", attrs.size()) }
                runLogging { res.put("sdk26Attrs", sdk26Attrs) }
            }
        }
    }



    if( mimetypeExtension!=null){
        val ctx = mimetypeExtension
        val uri = this
        val fullPath = uri.path

        val mimeType = ctx.contentResolver.getType(uri)
        val extension = MimeTypeMap.getFileExtensionFromUrl(fullPath)
        val extension2 = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val mimeType2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        val mimetype3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension2)

        runLogging { res.put("extension", extension) }
        runLogging { res.put("extension2", extension2) }
        runLogging { res.put("mimeType", mimeType) }
        runLogging { res.put("mimeType2", mimeType2) }
        runLogging { res.put("mimetype3", mimetype3) }

    }

    return res

}


/*

val u1 = if (SDK_INT >= 30)  MediaStore.Video.Media.getContentUri("",0) else  null
    val u2 = if (SDK_INT >= 30)  MediaStore.Images.Media.getContentUri("",0) else  null
    val u3 = if (SDK_INT >= 30)  MediaStore.Downloads.getContentUri("",0) else  null
    val u4 = if (SDK_INT >= 30)  MediaStore.Audio.Media.getContentUri("",0) else  null
    val mediaUris = listOf<Uri?>(
        u1,
        MediaStore.Video.Thumbnails.getContentUri(""),
        u2,
        MediaStore.Images.Thumbnails.getContentUri(""),
        MediaStore.Files.getContentUri("",0),
        u3,

        MediaStore.Audio.Albums.getContentUri(""),
        MediaStore.Audio.Artists.getContentUri(""),
        MediaStore.Audio.Genres.getContentUri(""),
        u4,
        MediaStore.Audio.Playlists.getContentUri(""),
        MediaStore.Audio.Artists.getContentUri(""),
        )

Major File DBs

content://downloads/all_downloads
content://downloads/my_downloads
content://downloads/public_downloads
content://media/external/file
content://media/internal/file

content://media/external/images/media
content://media/internal/images/media
content://media/external/audio/media
content://media/internal/audio/media
content://media/external/video/media
content://media/internal/video/media
content://media/external/docs/document
content://media/internal/docs/document
content://media/external/docs/pdf
content://media/internal/docs/pdf

content://com.android.providers.media.documents/document/
content://com.android.providers.media.documents/audio/
content://com.android.providers.media.documents/video/
content://com.android.providers.media.documents/image/
content://com.android.externalstorage.documents/document/
content://com.android.externalstorage.documents/audio/
content://com.android.externalstorage.documents/video/
content://com.android.externalstorage.documents/image/

content://com.android.providers.downloads.documents/document/
content://com.android.providers.downloads.documents/audio/
content://com.android.providers.downloads.documents/video/
content://com.android.providers.downloads.documents/image/
content://com.android.providers.downloads.documents/archive/
content://com.android.providers.downloads.documents/apk/
content://com.android.providers.downloads.documents/pdf/

* */

/*

Major Uris

content://com.android.browser/bookmarks
content://com.android.browser/history
content://com.android.calendar/attendees
content://com.android.calendar/calendar_alerts
content://com.android.calendar/calendar_cache
content://com.android.calendar/calendar_properties
content://com.android.calendar/calendars
content://com.android.calendar/events
content://com.android.calendar/experiments
content://com.android.calendar/instances
content://com.android.calendar/reminders
content://com.android.contacts/contacts
content://com.android.contacts/data
content://com.android.contacts/phone_lookup
content://com.android.contacts/raw_contacts
content://com.android.externalstorage.documents/document
content://com.android.externalstorage.documents/tree
content://com.android.launcher.settings/favorites
content://com.android.launcher2.settings/favorites
content://com.android.launcher3.settings/favorites
content://com.android.managedprovisioning/device_policy_manager_state
content://com.android.managedprovisioning/last_privisioning_timestamp
content://com.android.managedprovisioning/managed_profile
content://com.android.managedprovisioning/mdm_restrictions
content://com.android.managedprovisioning/mdm_state
content://com.android.managedprovisioning/owner_name
content://com.android.managedprovisioning/package_downloaded
content://com.android.managedprovisioning/profile_owner
content://com.android.managedprovisioning/provisioning_state
content://com.android.providers.blockednumber/blocked
content://com.android.providers.calendar/attendee_data
content://com.android.providers.calendar/calendar_location
content://com.android.providers.calendar/calendar_metadata
content://com.android.providers.calendar/experiments
content://com.android.providers.calendar/extended_properties
content://com.android.providers.calendar/timezone
content://com.android.providers.calendar/weekstart
content://com.android.providers.downloads/all_downloads
content://com.android.providers.downloads/app_downloads
content://com.android.providers.downloads/documents
content://com.android.providers.downloads/downloads
content://com.android.providers.downloads/external
content://com.android.providers.downloads/my_downloads
content://com.android.providers.downloads/ui
content://com.android.providers.drm/forwardlock
content://com.android.providers.media.documents/audio
content://com.android.providers.media.documents/document
content://com.android.providers.media.documents/image
content://com.android.providers.media.documents/picker
content://com.android.providers.media.documents/tree
content://com.android.providers.media.documents/video
content://com.android.providers.media.tv/channel
content://com.android.providers.media.tv/program
content://com.android.providers.media/albums
content://com.android.providers.media/audio_artists
content://com.android.providers.media/audio_genres
content://com.android.providers.media/audio_playlists
content://com.android.providers.media/video
content://com.android.providers.media/video/thumbnails
content://com.android.providers.settings/search_index
content://com.android.providers.telephony/carriers
content://com.android.providers.telephony/icc
content://com.android.providers.telephony/mms
content://com.android.providers.telephony/sms
content://com.android.providers.telephony/voicemail
content://com.android.providers.userdictionary/words
content://com.android.settings.accessibility/sound_vibration_settings
content://com.android.settings.accounts/
content://com.android.settings.applications.defaultapps/phone
content://com.android.settings.applications.defaultapps/sms
content://com.android.settings.applications.manageapplications/all
content://com.android.settings.applications.manageapplications/all/:userId
content://com.android.settings.applications.usagestats/interval
content://com.android.settings.applications.usagestats/summary
content://com.android.settings.applications.usagestats/usage
content://com.android.settings.applications/app_package_exclusion_list
content://com.android.settings.bluetooth/last_connected_device
content://com.android.settings.bluetooth_devices
content://com.android.settings.connecteddevice/audiosink
content://com.android.settings.connecteddevice/autolaunchapp
content://com.android.settings.connecteddevice/multisink_audio
content://com.android.settings.connecteddevice/paired_devices
content://com.android.settings.datausage/app_restrictions
content://com.android.settings.development/adb_enabled
content://com.android.settings.development/allow_non_market_apps
content://com.android.settings.development/development_prefs
content://com.android.settings.deviceinfo/baseband_version
content://com.android.settings.deviceinfo/boottime
content://com.android.settings.deviceinfo/build_date
content://com.android.settings.deviceinfo/display
content://com.android.settings.deviceinfo/firmware_version
content://com.android.settings.deviceinfo/hardware
content://com.android.settings.deviceinfo/kernel_version
content://com.android.settings.deviceinfo/notifications
content://com.android.settings.deviceinfo/security_patch
content://com.android.settings.deviceinfo/serialnumber
content://com.android.settings.deviceinfo/status
content://com.android.settings.deviceinfo/system_update
content://com.android.settings.deviceinfo/uptime
content://com.android.settings.fingerprint/lockscreen_prefs
content://com.android.settings.fingerprint/lockscreen_prefs/:userId
content://com.android.settings.fuelgauge.battery_settings
content://com.android.settings.fuelgauge.battery_status
content://com.android.settings.fuelgauge.battery_status/:userId
content://com.android.settings.fuelgauge.batterySaverMode
content://com.android.settings.fuelgauge.batterySaverTriggered
content://com.android.settings.fuelgauge.dump
content://com.android.settings.fuelgauge.history
content://com.android.settings.fuelgauge.history/detail
content://com.android.settings.fuelgauge.history/interval
content://com.android.settings.fuelgauge.PowerUsageSummaryProvider
content://com.android.settings.fuelgauge.unplugged_stats
content://com.android.settings.fuelgauge/batterytipstate
content://com.android.settings.fuelgauge/last_full_charge
content://com.android.settings.fuelgauge/uids
content://com.android.settings.gestures/assist_touch_settings
content://com.android.settings.gestures/assist_twice_in_row
content://com.android.settings.gestures/double_tap_screen
content://com.android.settings.gestures/double_tap_screen_doze
content://com.android.settings.gestures/double_tap_screen_doze_default
content://com.android.settings.gestures/double_tap_screen_light
content://com.android.settings.gestures/double_tap_screen_light_default
content://com.android.settings.gestures/secure
content://com.android.settings.homepage/choice_provider
content://com.android.settings.intelligence.suggestion_engagement_stats
content://com.android.settings.intelligence.suggestion_feedback
content://com.android.settings.intelligence.suggestion_history
content://com.android.settings.intelligence.suggestion_source_stats
content://com.android.settings.intelligence.suggestions
content://com.android.settings.languagepicker.AccountPreferenceActivity
content://com.android.settings.languagepicker.LocalePreferenceActivity
content://com.android.settings.localepicker.LocaleListEditor
content://com.android.settings.location/secure
content://com.android.settings.notification/BubblePreferenceActivity
content://com.android.settings.notification/channels
content://com.android.settings.notification/channels/:id
content://com.android.settings.notification/conditions
content://com.android.settings.notification/light_settings
content://com.android.settings.notification/notification_assistant_settings
content://com.android.settings.notification/notification_listener_settings
content://com.android.settings.notification/notification_policy_access_settings
content://com.android.settings.notification/notification_settings
content://com.android.settings.notification/zen_mode_config_etag
content://com.android.settings.notification/zen_mode_config_etag/:id
content://com.android.settings.notification/zen_mode_duration
content://com.android.settings.notification/zen_mode_enabled
content://com.android.settings.notification/zen_mode_fallback_config
content://com.android.settings.notification/zen_mode_schedule_rule
content://com.android.settings.notification/zen_mode_schedule_rule/:id
content://com.android.settings.notification/zen_mode_schedule_settings
content://com.android.settings.notification/zen_mode_setting
content://com.android.settings.notification/zen_mode_settings
content://com.android.settings.notification/zen_mode_settings_activity
content://com.android.settings.notification/zen_mode_visual_interruption_settings
content://com.android.settings.privacy/privacydashboardintent
content://com.android.settings.search/searchableitems
content://com.android.settings.search/searchableitems/:id
content://com.android.settings.security.certificates
content://com.android.settings.security.SecuritySettings
content://com.android.settings.security/screen_pinning_settings
content://com.android.settings.slices
content://com.android.settings.slices/action
content://com.android.settings.soundpicker.SoundPickerActivity
content://com.android.settings.soundpicker.SoundPickerPreferenceFragment
content://com.android.settings.suggested_apps
content://com.android.settings.support
content://com.android.settings.vpn2/config_editor
content://com.android.settings.wifi.savedaccesspoints
content://com.android.settings.wifi.savedaccesspoints/:id
content://com.android.settings.wifi_add_networks/:userId
content://com.android.settings.wifi_add_networks/associated
content://com.android.settings.wifi_add_networks/hidden
content://com.android.settings.wifi_add_networks/hidden/:userId
content://com.android.settings.wifi_add_networks/open
content://com.android.settings.wifi_add_networks/open/:userId
content://com.android.settings.wifi_add_networks/psk
content://com.android.settings.wifi_add_networks/psk/:userId
content://com.android.settings.wifi_add_networks/wep
content://com.android.settings.wifi_add_networks/wep/:userId
content://com.android.settings.wifi_calling
content://com.android.settings.zen.ZenModeAutomationSettings
content://com.android.settings.zen.ZenModeAutomationSettings/:id
content://com.android.settings.zen.ZenModeBlockedEffectsConfigActivity
content://com.android.settings.zen.ZenModeConditionProviderConfigActivity
content://com.android.settings.zen.ZenModeEventRuleSettings
content://com.android.settings.zen.ZenModeEventRuleSettings/:id
content://com.android.settings.zen.ZenModeExternalConditionConfigActivity
content://com.android.settings.zen.ZenModeExternalRuleConfigActivity
content://com.android.settings.zen.ZenModeExternalRuleSettings
content://com.android.settings.zen.ZenModeExternalRuleSettings/:id
content://com.android.settings.zen.ZenModePrioritySettings
content://com.android.settings.zen.ZenModeRuleSettings
content://com.android.settings.zen.ZenModeScheduleRuleSettings
content://com.android.settings.zen.ZenModeScheduleSettings
content://com.android.settings/notifications
content://com.android.settings/secure
content://com.android.settings/system
content://com.android.systemui.qs.QSTileStateProvider
content://com.android.systemui.qs.QSTileStateProvider/uid
content://com.android.systemui.qs.QSTileStateProvider/uid/:uid
content://com.android.systemui.settings.BrightnessController
content://com.android.systemui.settings.BrightnessController/:id
content://com.android.systemui.statusbar.NotificationDataContentProvider
content://com.android.systemui.statusbar.NotificationDataContentProvider/notifications
content://com.android.systemui.statusbar.NotificationDataContentProvider/notifications/:key
content://com.android.systemui.statusbar.policy.NetworkController
content://com.android.systemui.statusbar.policy.NetworkController/:id
content://com.android.systemui.statusbar.policy.SignalController
content://com.android.systemui.statusbar.policy.SignalController/:id
content://com.android.systemui.statusbar.policy.WifiController
content://com.android.systemui.statusbar.policy.WifiController/:id
content://com.android.voicemail/voicemail
content://com.android.voicemail/voicemail_dir
content://com.google.android.gms.chimera.container.GmsModuleFinder
content://com.google.android.gms.chimera.ContainerService
content://com.google.android.gms.common.config.GservicesProvider
content://com.google.android.gms.common.config.GservicesProvider/:key
content://com.google.android.gms.icing.proxy.TrieContentProvider
content://com.google.android.gms.instantapps.provider.InstantAppProvider
content://com.google.android.gms.maps.model.tileprovider.TileProviderUri
content://com.google.android.gms.nearby.bootstrap.state.DeviceScannerState
content://com.google.android.gms.people.sync.focus.PeopleSyncStatusProvider
content://com.google.android.gms.people.sync.focus.PeopleSyncStatusProvider/:account
content://com.google.android.gms.phenotype.FlagOverridesProvider
content://com.google.android.gms.phenotype.PhenotypeContentProvider
content://com.google.android.gms.phenotype.PhenotypeContentProvider/:app_id
content://com.google.android.gms.update.provider.UpdateProvider
content://com.google.android.gms.update.provider.UpdateProvider/:id
content://com.google.android.gsf.gservices.GoogleSettings
content://com.google.android.gsf.gservices.GoogleSettings/:key
content://com.google.android.gsf.gservices.PackagesProvider
content://com.google.android.inputmethod.latin.userdictionary.UserDictionaryProvider
content://com.google.android.music.MusicContent.Artists
content://com.google.android.music.MusicContent.Artists/:id
content://com.google.android.music.MusicContent.Genres
content://com.google.android.music.MusicContent.Genres/:id
content://com.google.android.music.MusicContent.Playlists
content://com.google.android.music.MusicContent.Playlists/:id
content://com.google.android.music.MusicContent.Podcasts
content://com.google.android.music.MusicContent.Podcasts/:id
content://com.google.android.music.MusicContent.SearchHistory
content://com.google.android.music.MusicContent.Songs
content://com.google.android.music.MusicContent.Songs/:id
content://com.google.android.music.MusicContent.TopCharts
content://com.google.android.music.MusicContent.TopCharts/:id
content://com.google.android.music.playback.AudioCastDataContentProvider
content://com.google.android.music.playback.MusicPlaybackStateProvider
content://com.google.android.music.playback.QueueProvider
content://com.google.android.music.playback.QueueProvider/:id
content://com.google.android.music.provider.MusicContent
content://com.google.android.music.ui.GenreListContentProvider
content://com.google.android.music.ui.GenreListContentProvider/:id
content://com.google.android.music.ui.PlaylistListContentProvider
content://com.google.android.music.ui.PlaylistListContentProvider/:id
content://com.google.android.music.ui.TrackListViewContentProvider
content://com.google.android.music.ui.TrackListViewContentProvider/:id
content://com.google.android.music.ui.TrackListViewContentProvider/:id/:tab
content://com.google.android.videos.contentprovider.TVProvider
content://com.google.android.videos.contentprovider.TVProvider/:id
content://com.google.android.videos.contentprovider.VideoProvider
content://com.google.android.videos.contentprovider.VideoProvider/:id
content://com.google.android.videos.contentprovider.VideoProvider/:id/:tab
content://com.google.android.videos.contentprovider.VideoProvider/:id/:tab/:sub_tab
content://com.google.android.videos.provider.VideosContentProvider
content://com.google.android.videos.provider.VideosContentProvider/:id
content://com.google.android.wearable.provider.WearableProvider
content://com.google.android.wearable.provider.WearableProvider/:id
content://com.google.android.wearable.provider.WearableProvider/:id/:path



*/

