package com.timenw.drinktracker.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.timenw.drinktracker.data.model.DailyDrinkSummary
import com.timenw.drinktracker.data.model.DrinkType
import com.timenw.drinktracker.data.repository.DrinkRepository
import com.timenw.drinktracker.ui.components.SummaryCard
import com.timenw.drinktracker.ui.theme.AlcoholDanger
import com.timenw.drinktracker.ui.theme.AlcoholSafe
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsTab(
    weeklyData: List<DailyDrinkSummary>,
    monthlyData: List<DailyDrinkSummary>,
    drinkFrequency: Map<String, Int>,
    categoryTotals: List<DrinkRepository.CategoryTotal>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("数据统计", fontWeight = FontWeight.Bold)
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 本周总览
            item {
                val totalAlcohol = weeklyData.sumOf { it.totalAlcoholGrams.toDouble() }.toFloat()
                val totalMl = weeklyData.sumOf { it.totalMl }
                val totalDrinks = weeklyData.sumOf { it.drinkCount }
                val avgDaily = if (weeklyData.isNotEmpty()) totalAlcohol / 7f else 0f
                val overLimitDays = weeklyData.count { it.isOverLimit }

                Text("本周总览", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    SummaryCard(title = "总酒精", value = "${totalAlcohol.toInt()}g", modifier = Modifier.weight(1f), emoji = "🧪")
                    Spacer(modifier = Modifier.width(8.dp))
                    SummaryCard(title = "总容量", value = "${totalMl}ml", modifier = Modifier.weight(1f), emoji = "🥤")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    SummaryCard(title = "日均酒精", value = "${avgDaily.toInt()}g", modifier = Modifier.weight(1f), emoji = "📊")
                    Spacer(modifier = Modifier.width(8.dp))
                    SummaryCard(title = "超标天数", value = "${overLimitDays}天", modifier = Modifier.weight(1f), emoji = "⚠️")
                }
            }

            // 本周柱状图
            item {
                Text("本周饮酒趋势", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (weeklyData.all { it.totalAlcoholGrams == 0f }) {
                            Text(
                                text = "暂无数据，开始记录啤酒吧 🍺",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )
                        } else {
                            AlcoholBarChart(weeklyData)
                        }
                    }
                }
            }

            // ===== 每类酒总饮用统计（从第一次到最后一次） =====
            item {
                Text("各类酒总饮用统计", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "统计时间：从第一次饮酒记录到最后一次",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (categoryTotals.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "还没有饮酒记录，开始记录啤酒吧 🍺",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            } else {
                items(categoryTotals.size) { index ->
                    val ct = categoryTotals[index]
                    val drinkType = try { DrinkType.valueOf(ct.drinkType) } catch (e: Exception) { DrinkType.OTHER }
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // 酒类名称和图标
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = drinkType.emoji, style = MaterialTheme.typography.headlineSmall)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = drinkType.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            // 统计数据
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("总容量", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${ct.totalMl}ml", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("纯酒精", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${ct.totalAlcoholGrams.toInt()}g", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("饮用次数", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${ct.drinkCount}次", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(8.dp))

                            // 时间跨度
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("首次饮用", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = formatDateShort(ct.firstDate),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("最近饮用", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = formatDateShort(ct.lastDate),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("时间跨度", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        text = if (ct.daysSpan == 0) "首次" else "${ct.daysSpan}天",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 酒类偏好（最近30天）
            item {
                Text("酒类偏好（近30天）", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (drinkFrequency.isEmpty()) {
                            Text(
                                text = "还没有饮酒记录",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            drinkFrequency.entries.sortedByDescending { it.value }.take(5).forEach { (typeName, count) ->
                                val drinkType = try { DrinkType.valueOf(typeName) } catch (e: Exception) { null }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${drinkType?.emoji ?: "🥤"} ${drinkType?.displayName ?: typeName}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${count}次",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 健康提示
            item {
                Text("健康提示", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🍺 中国膳食指南建议：", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• 成年男性每日不超过 25g 纯酒精", style = MaterialTheme.typography.bodySmall)
                        Text("• 成年女性每日不超过 15g 纯酒精", style = MaterialTheme.typography.bodySmall)
                        Text("• 孕妇、未成年人、驾驶员请勿饮酒", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💡 1标准杯 ≈ 10g 纯酒精 ≈ 啤酒330ml ≈ 红酒150ml ≈ 白酒30ml", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

private fun formatDateShort(dateStr: String): String {
    return try {
        val parts = dateStr.split("-")
        if (parts.size == 3) "${parts[1]}/${parts[2]}" else dateStr
    } catch (e: Exception) { dateStr }
}

@Composable
fun AlcoholBarChart(data: List<DailyDrinkSummary>) {
    val maxAlcohol = data.maxOfOrNull { it.totalAlcoholGrams } ?: 20f
    val dayFormatter = SimpleDateFormat("E", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { summary ->
            val barHeight = (summary.totalAlcoholGrams / maxAlcohol.coerceAtLeast(1f)).coerceIn(0f, 1f)
            val isOver = summary.isOverLimit

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${summary.totalAlcoholGrams.toInt()}g",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(100.dp)
                ) {
                    val barWidth = size.width
                    val barH = size.height * barHeight
                    drawRect(
                        color = if (isOver) AlcoholDanger else AlcoholSafe,
                        topLeft = Offset(0f, size.height - barH),
                        size = androidx.compose.ui.geometry.Size(barWidth, barH)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = try {
                        dayFormatter.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(summary.date) ?: Date())
                    } catch (e: Exception) { summary.date.takeLast(2) },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
