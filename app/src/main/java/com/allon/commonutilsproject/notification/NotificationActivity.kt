package com.allon.commonutilsproject.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.view.View
import com.allon.commonutilsproject.R
import com.cyou.xiyou.cyou.common.notification.NotificationUtil
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun start(ctx: Context) {
            ctx.startActivity(Intent(ctx, NotificationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        NotificationUtil.init(this)
        base.setOnClickListener(this)
        collapsed.setOnClickListener(this)
        headsup.setOnClickListener(this)
        progress.setOnClickListener(this)
        bigText.setOnClickListener(this)
    }

    override fun onClick(v: View) {
      when(v.id) {
          R.id.base -> {
              val intent = NotificationUtil.getRegularPendingIntent(ResultActivity::class.java)
              NotificationUtil.notify("channledId", "Base Intification", "i am base interfication", "it is really basic", R.drawable.nofi_icon, true, intent, NotificationCompat.PRIORITY_DEFAULT)
          }

          R.id.collapsed -> {
              val collBuilder = NotificationUtil.createBaseBuilder("collapsed", "折叠通知", "折叠内容", R.drawable.nofi_icon)
              collBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.pic)).setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.drawable.pic)).bigLargeIcon(null))
              NotificationUtil.notify(collBuilder.build(), 0)
          }

          R.id.headsup -> {
//这种通知在即时通信的推送消息中用的会比较多
              //text();
              //有的手机可能无法弹出，一加5可以弹出
              NotificationUtil.sChannledImportant = NotificationManager.IMPORTANCE_HIGH
              val baseBuilder = NotificationUtil.createBaseBuilder("float", "悬浮通知", "悬浮内容", R.drawable.pic)

              NotificationUtil.notify(baseBuilder.build(), 0)
          }

          R.id.progress -> {
              progressNotification()
          }

          R.id.bigText -> {
              val bugTextBuilder = NotificationUtil.createBaseBuilder("bigText", "大文本通知", "大文本内容", R.drawable.nofi_icon)
              bugTextBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.pic)).setStyle(NotificationCompat.BigTextStyle().bigText("t text tetxtetxtetxtdtxtetxtetxttt"))
              NotificationUtil.notify(bugTextBuilder.build(), 0)
          }
      }
    }

    private fun progressNotification() {

        Thread(Runnable {
            NotificationUtil.notifyWithProgress("progress", "", R.drawable.nofi_icon) { manager, builder, id ->
                var title = "下载中"
                var i = 0
                while (i <= 100) {
                    builder.setContentTitle(title).setProgress(100, i, false)
                    println("i = $i")
                    SystemClock.sleep(1000)
                    manager.notify(id, builder.build())
                    i += 10
                }
                title = "下载完成"
                builder.setContentTitle(title).setProgress(0, 0, false)
                manager.notify(id, builder.build())
                //下载完成后可以做其他的事情了
            }
        }).start()
    }
}
