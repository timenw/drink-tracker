package com.timenw.drinktracker.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timenw.drinktracker.data.model.*
import java.time.LocalDate

class DrinkRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("drink_tracker", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ===== 饮酒记录 =====
    fun getDrinkRecords(date: LocalDate = LocalDate.now()): List<DrinkRecord> {
        val key = "drinks_${date}"
        val json = prefs.getString(key, "[]") ?: "[]"
        val type = object : TypeToken<List<DrinkRecord>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun addDrinkRecord(record: DrinkRecord) {
        val records = getDrinkRecords(LocalDate.parse(record.date)).toMutableList()
        records.add(record.copy(alcoholGrams = record.calcAlcoholGrams()))
        saveDrinkRecords(records, LocalDate.parse(record.date))
    }

    fun removeDrinkRecord(id: Long, date: LocalDate = LocalDate.now()) {
        val records = getDrinkRecords(date).toMutableList()
        records.removeAll { it.id == id }
        saveDrinkRecords(records, date)
    }

    private fun saveDrinkRecords(records: List<DrinkRecord>, date: LocalDate) {
        val key = "drinks_${date}"
        prefs.edit().putString(key, gson.toJson(records)).apply()
    }

    // ===== 统计 =====
    fun getDailySummary(date: LocalDate = LocalDate.now()): DailyDrinkSummary {
        val records = getDrinkRecords(date)
        val totalMl = records.sumOf { it.amountMl }
        val totalAlcohol = records.sumOf { it.alcoholGrams.toDouble() }.toFloat()
        return DailyDrinkSummary(
            date = date.toString(),
            totalMl = totalMl,
            totalAlcoholGrams = totalAlcohol,
            drinkCount = records.size,
            records = records
        )
    }

    fun getWeeklyData(): List<DailyDrinkSummary> {
        val today = LocalDate.now()
        return (0..6).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            getDailySummary(date)
        }.reversed()
    }

    fun getMonthlyData(): List<DailyDrinkSummary> {
        val today = LocalDate.now()
        return (0..29).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            getDailySummary(date)
        }.reversed()
    }

    // 获取指定日期范围内的饮酒频率统计
    fun getDrinkFrequency(days: Int = 30): Map<String, Int> {
        val today = LocalDate.now()
        val freq = mutableMapOf<String, Int>()
        for (i in 0 until days) {
            val date = today.minusDays(i.toLong())
            val records = getDrinkRecords(date)
            records.forEach { record ->
                val type = record.drinkType
                freq[type] = (freq[type] ?: 0) + 1
            }
        }
        return freq
    }

    // ===== 按类别统计（从第一次到最后一次） =====
    data class CategoryTotal(
        val drinkType: String,
        val totalMl: Int,
        val totalAlcoholGrams: Float,
        val drinkCount: Int,
        val firstDate: String,
        val lastDate: String,
        val daysSpan: Int
    )

    fun getCategoryTotals(): List<CategoryTotal> {
        // 扫描所有日期的记录（从最早到最近365天）
        val today = LocalDate.now()
        val earliestDate = today.minusDays(365)
        val categoryMap = mutableMapOf<String, MutableList<DrinkRecord>>()

        var date = earliestDate
        while (!date.isAfter(today)) {
            val records = getDrinkRecords(date)
            records.forEach { record ->
                categoryMap.getOrPut(record.drinkType) { mutableListOf() }.add(record)
            }
            date = date.plusDays(1)
        }

        return categoryMap.map { (typeName, records) ->
            val totalMl = records.sumOf { it.amountMl }
            val totalAlcohol = records.sumOf { it.alcoholGrams.toDouble() }.toFloat()
            val dates = records.map { it.date }.sorted()
            val firstDate = dates.firstOrNull() ?: today.toString()
            val lastDate = dates.lastOrNull() ?: today.toString()
            val daysSpan = try {
                val first = LocalDate.parse(firstDate)
                val last = LocalDate.parse(lastDate)
                java.time.temporal.ChronoUnit.DAYS.between(first, last).toInt()
            } catch (e: Exception) { 0 }

            CategoryTotal(
                drinkType = typeName,
                totalMl = totalMl,
                totalAlcoholGrams = totalAlcohol,
                drinkCount = records.size,
                firstDate = firstDate,
                lastDate = lastDate,
                daysSpan = daysSpan
            )
        }.sortedByDescending { it.totalMl }
    }

    // ===== 设置 =====
    fun getSettings(): UserSettings {
        val json = prefs.getString("settings", null)
        return if (json != null) {
            gson.fromJson(json, UserSettings::class.java)
        } else {
            UserSettings()
        }
    }

    fun saveSettings(settings: UserSettings) {
        prefs.edit().putString("settings", gson.toJson(settings)).apply()
    }
}
