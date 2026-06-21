package com.timenw.drinktracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timenw.drinktracker.data.model.*
import com.timenw.drinktracker.ui.components.AlcoholProgressRing
import com.timenw.drinktracker.ui.components.EmptyStateView
import com.timenw.drinktracker.ui.components.SummaryCard
import com.timenw.drinktracker.ui.theme.AlcoholDanger
import com.timenw.drinktracker.ui.theme.AlcoholSafe
import com.timenw.drinktracker.ui.theme.AlcoholWarning
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkHomeTab(
    summary: DailyDrinkSummary,
    targetGrams: Float,
    records: List<DrinkRecord>,
    selectedType: DrinkType,
    selectedAbv: Float,
    onSelectedTypeChanged: (DrinkType) -> Unit,
    onSelectedAbvChanged: (Float) -> Unit,
    onAddDrink: (DrinkType, Int, Float) -> Unit,
    onRemoveRecord: (Long) -> Unit
) {
    var showCustomDialog by remember { mutableStateOf(false) }
    var customAmount by remember { mutableStateOf("") }
    var customAbv by remember { mutableStateOf(selectedType.abvDefault.toString()) }
    var showTypeSelector by remember { mutableStateOf(false) }

    val formatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalBar, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("饮酒统计", fontWeight = FontWeight.Bold)
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 进度环
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlcoholProgressRing(
                        progress = summary.totalAlcoholGrams / targetGrams,
                        alcoholGrams = summary.totalAlcoholGrams,
                        targetGrams = targetGrams,
                        size = 160,
                        strokeWidth = 12
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "今日已饮酒 ${summary.drinkCount} 次",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (summary.totalAlcoholGrams > targetGrams) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⚠️ 已超过戒酒目标",
                            style = MaterialTheme.typography.labelMedium,
                            color = AlcoholDanger
                        )
                    }
                }
            }

            // 统计卡片
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SummaryCard(
                        title = "总容量",
                        value = "${summary.totalMl}ml",
                        modifier = Modifier.weight(1f),
                        emoji = "🥤"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SummaryCard(
                        title = "纯酒精",
                        value = "${summary.totalAlcoholGrams.toInt()}g",
                        modifier = Modifier.weight(1f),
                        emoji = "🧪"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SummaryCard(
                        title = "杯数",
                        value = "${summary.drinkCount}",
                        modifier = Modifier.weight(1f),
                        emoji = "🍻"
                    )
                }
            }

            // 酒类选择器
            item {
                Text(
                    text = "选择酒类",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                DrinkTypeSelector(
                    selectedType = selectedType,
                    onTypeSelected = {
                        onSelectedTypeChanged(it)
                        onSelectedAbvChanged(it.abvDefault)
                    }
                )
            }

            // 酒精度数设置
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "酒精度数: ${selectedAbv}%",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "参考范围: ${selectedType.abvMin}% ~ ${selectedType.abvMax}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = selectedAbv,
                            onValueChange = { onSelectedAbvChanged(it) },
                            valueRange = selectedType.abvMin..selectedType.abvMax,
                            steps = ((selectedType.abvMax - selectedType.abvMin) / 0.5f).toInt().coerceAtLeast(1)
                        )
                    }
                }
            }

            // 快速添加按钮
            item {
                Text(
                    text = "快速添加",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            item {
                QuickAddGrid(
                    drinkType = selectedType,
                    onAddDrink = { amount -> onAddDrink(selectedType, amount, selectedAbv) }
                )
            }

            // 自定义添加
            item {
                OutlinedButton(
                    onClick = {
                        customAmount = ""
                        customAbv = selectedAbv.toString()
                        showCustomDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("自定义酒量")
                }
            }

            // 今日记录
            item {
                Text(
                    text = "今日记录",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (records.isEmpty()) {
                item {
                    EmptyStateView(
                        emoji = "🍺",
                        title = "还没有饮酒记录",
                        subtitle = "选择一款酒，开始记录吧"
                    )
                }
            } else {
                items(records.reversed(), key = { it.id }) { record ->
                    val drinkType = try { DrinkType.valueOf(record.drinkType) } catch (e: Exception) { DrinkType.OTHER }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = drinkType.emoji, fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${drinkType.displayName} ${record.amountMl}ml (${record.abv}%)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${record.alcoholGrams.toInt()}g 酒精 · ${formatter.format(Date(record.timestamp))}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { onRemoveRecord(record.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "删除", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    // 自定义对话框
    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("自定义酒量") },
            text = {
                Column {
                    Text("酒类: ${selectedType.displayName}")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customAmount,
                        onValueChange = { customAmount = it.filter { c -> c.isDigit() } },
                        label = { Text("容量 (ml)") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customAbv,
                        onValueChange = { newVal ->
                            // 允许数字和小数点
                            customAbv = newVal.filter { c -> c.isDigit() || c == '.' }
                        },
                        label = { Text("酒精度数 (%)") },
                        singleLine = true
                    )
                    Text(
                        text = "参考: ${selectedType.abvMin}%~${selectedType.abvMax}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val ml = customAmount.toIntOrNull()
                        val abv = customAbv.toFloatOrNull()
                        if (ml != null && ml > 0 && abv != null && abv >= 0) {
                            onAddDrink(selectedType, ml, abv)
                            customAmount = ""
                            showCustomDialog = false
                        }
                    }
                ) { Text("添加") }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false; customAmount = "" }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun DrinkTypeSelector(
    selectedType: DrinkType,
    onTypeSelected: (DrinkType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("${selectedType.emoji} ${selectedType.displayName}  (参考 ${selectedType.abvDefault}%)")
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            // 直接显示所有大类，按分组排列
            val groups = listOf(
                listOf(DrinkType.BEER, DrinkType.WINE, DrinkType.HUANGJIU, DrinkType.SAKE, DrinkType.CIDER),
                listOf(DrinkType.BAIJIU, DrinkType.WHISKY, DrinkType.BRANDY, DrinkType.VODKA, DrinkType.RUM),
                listOf(DrinkType.TEQUILA, DrinkType.GIN, DrinkType.SOJU, DrinkType.COCKTAIL, DrinkType.LIQUEUR, DrinkType.OTHER)
            )
            groups.forEach { group ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    group.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = {
                                onTypeSelected(type)
                                expanded = false
                            },
                            label = { Text("${type.emoji} ${type.displayName}", style = MaterialTheme.typography.labelMedium) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
fun QuickAddGrid(
    drinkType: DrinkType,
    onAddDrink: (Int) -> Unit
) {
    // 根据酒类生成快速添加容量
    val amounts = when (drinkType.category) {
        DrinkCategory.BEER -> listOf(250, 330, 500, 1000)
        DrinkCategory.WINE -> listOf(100, 150, 200, 750)
        DrinkCategory.BAIJIU -> listOf(30, 50, 100, 150)
        DrinkCategory.WHISKY, DrinkCategory.BRANDY, DrinkCategory.VODKA,
        DrinkCategory.GIN, DrinkCategory.RUM, DrinkCategory.TEQUILA -> listOf(30, 60, 90, 120)
        DrinkCategory.SAKE -> listOf(90, 180, 360, 720)
        DrinkCategory.SOJU -> listOf(50, 100, 150, 360)
        DrinkCategory.COCKTAIL -> listOf(60, 120, 180, 240)
        DrinkCategory.LIQUEUR -> listOf(30, 60, 90, 120)
        DrinkCategory.HUANGJIU -> listOf(100, 150, 200, 500)
        DrinkCategory.CIDER -> listOf(250, 330, 500, 750)
        DrinkCategory.OTHER -> listOf(100, 200, 300, 500)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val rows = amounts.chunked(4)
        rows.forEach { rowAmounts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowAmounts.forEach { amount ->
                    FilledTonalButton(
                        onClick = { onAddDrink(amount) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "${amount}ml",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                repeat(4 - rowAmounts.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
