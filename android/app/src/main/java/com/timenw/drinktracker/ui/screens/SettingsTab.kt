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

            // 戒酒目标
            item {
                Text("戒酒目标", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "每日纯酒精上限: ${settings.dailyAlcoholTargetGrams.toInt()}g",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = settings.dailyAlcoholTargetGrams,
                            onValueChange = { onSettingsChanged(settings.copy(dailyAlcoholTargetGrams = it)) },
                            valueRange = 5f..50f,
                            steps = 8
                        )
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("5g", style = MaterialTheme.typography.labelSmall)
                            Text("50g", style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 中国膳食指南建议：男性≤25g/天，女性≤15g/天",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))

                        // 达到目标提醒开关
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("达到目标提醒", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text(
                                    text = if (settings.targetEnabled) "已开启 — 超过目标时提醒" else "已关闭",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = settings.targetEnabled,
                                onCheckedChange = { onSettingsChanged(settings.copy(targetEnabled = it)) }
                            )
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
