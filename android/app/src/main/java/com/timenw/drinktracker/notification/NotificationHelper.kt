package com.timenw.drinktracker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.timenw.drinktracker.R

object NotificationHelper {
    private const val CHANNEL_ID = "drink_tracker_channel"
    private const val CHANNEL_NAME = "饮酒提醒"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "饮酒统计提醒"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun sendTargetReachedNotification(context: Context, currentGrams: Float, targetGrams: Float) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("⚠️ 已达到戒酒目标")
            .setContentText("今日已摄入 ${currentGrams.toInt()}g 纯酒精，超过目标 ${targetGrams.toInt()}g，建议停止饮酒")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        manager.notify(1001, notification)
    }
}
