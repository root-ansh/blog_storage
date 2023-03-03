package work.curioustools.pdfwidget.utils

import android.os.Handler
import android.os.Looper


class QueueTask<T>(
    val run: () -> T,
    val timeout:Long? = null,
    val onSuccess:((T)->Unit)? = null,
    val onFailure: ((Throwable)->Unit)? = null
)

class AppExecutor private constructor(){

    private fun <T> executeWithTimeoutInParallelAndRunCallbacksOnMainThread(task: QueueTask<T>) {
        val t = Thread{
            val result: Result<T> = kotlin.runCatching { task.run() }

            Handler(Looper.getMainLooper()).post {
                if (result.isSuccess) task.onSuccess?.invoke(result.getOrThrow())
                else task.onFailure?.invoke(result.exceptionOrNull() ?: Exception("null"))
            }

            println("thread:${Looper.myLooper()?.thread?.id} started with  ${if(this== singleton) "singleton" else "new"} finished")

        }

        println("starting thread:${t.id} with ${if(this== singleton) "singleton" else "new"} instance")
        t.start()

    }

    fun <T> execute(task:QueueTask<T>){
         executeWithTimeoutInParallelAndRunCallbacksOnMainThread(task)
    }

    fun <T> execute(task: ()->T){
        val queueTask = QueueTask(task)
        execute(queueTask)
    }

    companion object{
        private var singleton: AppExecutor? = null


        fun sharedQueueTask(): AppExecutor {

            if(singleton !=null) return singleton!!
            synchronized(SafePrefs::class.java){
                if(singleton !=null)return singleton!!
                else{
                    singleton = AppExecutor()
                    singleton
                    return singleton!!
                }
            }
        }

        fun ioTask() = AppExecutor()

    }
}