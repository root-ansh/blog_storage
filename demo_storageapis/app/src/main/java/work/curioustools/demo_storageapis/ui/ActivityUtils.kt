package work.curioustools.demo_storageapis.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

interface ActivityUtils{
    fun AppCompatActivity.toast(str:String){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}