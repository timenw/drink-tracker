package com.timenw.drinktracker.data.model

/**
 * 酒类大类枚举 — 只保留大类，细分品类去掉
 * 每次饮酒时自定义填写酒精度数
 */
enum class DrinkType(
    val displayName: String,
    val emoji: String,
    val category: DrinkCategory,
    val abvMin: Float,      // 最低酒精度 %（参考值）
    val abvMax: Float,      // 最高酒精度 %（参考值）
    val abvDefault: Float,  // 默认酒精度 %（用户可修改）
    val standardMl: Int     // 标准杯容量 ml（参考值）
) {
    // ===== 发酵酒 =====
    BEER("啤酒", "🍺", DrinkCategory.BEER, 3f, 12f, 5f, 330),
    WINE("葡萄酒", "🍷", DrinkCategory.WINE, 10f, 16f, 13f, 150),
    HUANGJIU("黄酒", "🏺", DrinkCategory.HUANGJIU, 14f, 20f, 16f, 100),
    SAKE("清酒", "🍶", DrinkCategory.SAKE, 15f, 20f, 16f, 90),
    CIDER("果酒", "🍎", DrinkCategory.CIDER, 4f, 14f, 8f, 330),

    // ===== 蒸馏酒 =====
    BAIJIU("白酒", "🥃", DrinkCategory.BAIJIU, 38f, 65f, 52f, 50),
    WHISKY("威士忌", "🥃", DrinkCategory.WHISKY, 40f, 55f, 40f, 30),
    BRANDY("白兰地", "🥃", DrinkCategory.BRANDY, 36f, 45f, 40f, 30),
    VODKA("伏特加", "🍸", DrinkCategory.VODKA, 35f, 50f, 40f, 30),
    RUM("朗姆酒", "🥥", DrinkCategory.RUM, 35f, 50f, 40f, 30),
    TEQUILA("龙舌兰", "🌵", DrinkCategory.TEQUILA, 35f, 55f, 38f, 30),
    GIN("金酒", "🍸", DrinkCategory.GIN, 37f, 50f, 40f, 30),

    // ===== 配制酒 =====
    SOJU("烧酒", "🍶", DrinkCategory.SOJU, 16f, 25f, 17f, 50),
    COCKTAIL("鸡尾酒", "🍹", DrinkCategory.COCKTAIL, 5f, 25f, 15f, 120),
    LIQUEUR("利口酒", "🍸", DrinkCategory.LIQUEUR, 15f, 40f, 25f, 30),
    OTHER("其他", "🥤", DrinkCategory.OTHER, 0f, 100f, 40f, 100);

    companion object {
        fun getByCategory(category: DrinkCategory): List<DrinkType> {
            return values().filter { it.category == category }
        }
    }
}

enum class DrinkCategory(val displayName: String, val emoji: String) {
    BEER("啤酒", "🍺"),
    WINE("葡萄酒", "🍷"),
    HUANGJIU("黄酒", "🏺"),
    SAKE("清酒", "🍶"),
    CIDER("果酒", "🍎"),
    BAIJIU("白酒", "🥃"),
    WHISKY("威士忌", "🥃"),
    BRANDY("白兰地", "🥃"),
    VODKA("伏特加", "🍸"),
    RUM("朗姆酒", "🥥"),
    TEQUILA("龙舌兰", "🌵"),
    GIN("金酒", "🍸"),
    SOJU("烧酒", "🍶"),
    COCKTAIL("鸡尾酒", "🍹"),
    LIQUEUR("利口酒", "🍸"),
    OTHER("其他", "🥤")
}

/**
 * 饮酒记录
 */
data class DrinkRecord(
    val id: Long = System.currentTimeMillis(),
    val drinkType: String = DrinkType.BEER_REGULAR.name,
    val amountMl: Int = 330,
    val abv: Float = 5f,
    val alcoholGrams: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val date: String = java.time.LocalDate.now().toString(),
    val note: String = "",
    val scene: DrinkScene = DrinkScene.OTHER
) {
    fun calcAlcoholGrams(): Float {
        return amountMl * (abv / 100f) * 0.789f  // 酒精密度 0.789 g/ml
    }
}

/**
 * 饮酒场景
 */
enum class DrinkScene(val displayName: String, val emoji: String) {
    ALONE("独酌", "🧘"),
    PAIR("对饮", "👥"),
    GATHERING("聚会", "🎉"),
    BUSINESS("商务", "💼"),
    FESTIVAL("节庆", "🎊"),
    DINNER("佐餐", "🍽️"),
    CELEBRATION("庆祝", "🎂"),
    BAR("酒吧", "🍸"),
    OTHER("其他", "📍")
}

/**
 * 用户设置
 */
data class UserSettings(
    val dailyAlcoholTargetGrams: Float = 20f,  // 每日戒酒目标 g（超过此值提醒）
    val targetEnabled: Boolean = true,          // 是否启用戒酒目标提醒
    val weightKg: Float = 70f,
    val gender: Gender = Gender.MALE,
    val showAlcoholWarning: Boolean = true
)

enum class Gender(val displayName: String) {
    MALE("男"),
    FEMALE("女")
}

/**
 * 每日饮酒统计
 */
data class DailyDrinkSummary(
    val date: String = java.time.LocalDate.now().toString(),
    val totalMl: Int = 0,
    val totalAlcoholGrams: Float = 0f,
    val drinkCount: Int = 0,
    val records: List<DrinkRecord> = emptyList()
) {
    val isOverLimit: Boolean get() = totalAlcoholGrams > 20f  // 超过戒酒目标
    val progressPercent: Float get() = (totalAlcoholGrams / 20f * 100).coerceIn(0f, 150f)
}
