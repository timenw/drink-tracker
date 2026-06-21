package com.timenw.drinktracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.timenw.drinktracker.data.model.Gender
import com.timenw.drinktracker.data.model.UserSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTab(
    settings: UserSettings,
    onSettingsChanged: (UserSettings) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("设置", fontWeight = FontWeight.Bold)
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 个人信息
            item {
                Text("个人信息", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // 性别选择
                        Text("性别", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Gender.values().forEach { gender ->
                                FilterChip(
                                    selected = settings.gender == gender,
                                    onClick = { onSettingsChanged(settings.copy(gender = gender)) },
                                    label = { Text(gender.displayName) },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // 体重
                        Text("体重: ${settings.weightKg.toInt()} kg", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = settings.weightKg,
                            onValueChange = { onSettingsChanged(settings.copy(weightKg = it)) },
                            valueRange = 40f..150f,
                            steps = 109
                        )
                    }
                }
            }

            // 饮酒目标
            item {
                Text("饮酒目标", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "每日纯酒精目标: ${settings.dailyAlcoholTargetGrams.toInt()}g",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = settings.dailyAlcoholTargetGrams,
                            onValueChange = { onSettingsChanged(settings.copy(dailyAlcoholTargetGrams = it)) },
                            valueRange = 10f..50f,
                            steps = 7
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("10g", style = MaterialTheme.typography.labelSmall)
                            Text("50g", style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 中国膳食指南建议：男性≤25g/天，女性≤15g/天",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 提醒设置
            item {
                Text("提醒设置", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("饮酒提醒", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text(
                                    text = if (settings.reminderEnabled) "已开启" else "已关闭",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = settings.reminderEnabled,
                                onCheckedChange = { onSettingsChanged(settings.copy(reminderEnabled = it)) }
                            )
                        }

                        if (settings.reminderEnabled) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("提醒间隔: ${settings.reminderIntervalMinutes} 分钟")
                            Slider(
                                value = settings.reminderIntervalMinutes.toFloat(),
                                onValueChange = { onSettingsChanged(settings.copy(reminderIntervalMinutes = it.toInt())) },
                                valueRange = 30f..240f,
                                steps = 6
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("30分钟", style = MaterialTheme.typography.labelSmall)
                                Text("240分钟", style = MaterialTheme.typography.labelSmall)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("免打扰时段: ${settings.wakeUpHour}:00 - ${settings.sleepHour}:00")
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("起床时间", style = MaterialTheme.typography.labelSmall)
                                    Slider(
                                        value = settings.wakeUpHour.toFloat(),
                                        onValueChange = { onSettingsChanged(settings.copy(wakeUpHour = it.toInt())) },
                                        valueRange = 5f..12f,
                                        steps = 6
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("睡觉时间", style = MaterialTheme.typography.labelSmall)
                                    Slider(
                                        value = settings.sleepHour.toFloat(),
                                        onValueChange = { onSettingsChanged(settings.copy(sleepHour = it.toInt())) },
                                        valueRange = 20f..24f,
                                        steps = 3
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 关于
            item {
                Text("关于", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("饮酒统计", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                        Text("版本 1.0.0", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "一款专为爱酒之人设计的饮酒统计工具，帮助你理性饮酒、享受酒文化。",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
