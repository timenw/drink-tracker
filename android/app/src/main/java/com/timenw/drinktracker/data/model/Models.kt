package com.timenw.drinktracker.data.model

/**
 * 酒类枚举 — 涵盖世界主要酒类
 * 每种酒包含：名称、图标、酒精度范围、标准杯容量、每杯纯酒精量(g)
 */
enum class DrinkType(
    val displayName: String,
    val emoji: String,
    val category: DrinkCategory,
    val abvMin: Float,      // 最低酒精度 %
    val abvMax: Float,      // 最高酒精度 %
    val abvDefault: Float,  // 默认酒精度 %
    val standardMl: Int,    // 标准杯容量 ml
    val alcoholPerStandard: Float  // 每标准杯纯酒精 g
) {
    // ===== 啤酒类 =====
    BEER_LIGHT("淡啤酒", "🍺", DrinkCategory.BEER, 3f, 5f, 4f, 330, 10f),
    BEER_REGULAR("普通啤酒", "🍺", DrinkCategory.BEER, 4f, 6f, 5f, 330, 13f),
    BEER_STRONG("烈性啤酒", "🍺", DrinkCategory.BEER, 6f, 12f, 8f, 330, 21f),
    BEER_CRAFT("精酿啤酒", "🍺", DrinkCategory.BEER, 5f, 10f, 7f, 330, 18f),

    // ===== 葡萄酒类 =====
    WINE_RED("红葡萄酒", "🍷", DrinkCategory.WINE, 12f, 16f, 13f, 150, 15f),
    WINE_WHITE("白葡萄酒", "🥂", DrinkCategory.WINE, 10f, 14f, 12f, 150, 14f),
    WINE_ROSE("桃红葡萄酒", "🌸", DrinkCategory.WINE, 10f, 14f, 12f, 150, 14f),
    WINE_SPARKLING("起泡酒", "🍾", DrinkCategory.WINE, 10f, 13f, 12f, 150, 14f),

    // ===== 白酒类 =====
    BAIJIU_LIGHT("低度白酒", "🥃", DrinkCategory.BAIJIU, 38f, 42f, 38f, 50, 15f),
    BAIJIU_REGULAR("普通白酒", "🥃", DrinkCategory.BAIJIU, 42f, 52f, 52f, 50, 20f),
    BAIJIU_STRONG("高度白酒", "🥃", DrinkCategory.BAIJIU, 52f, 65f, 56f, 50, 22f),

    // ===== 威士忌类 =====
    WHISKY_SCOTCH("苏格兰威士忌", "🥃", DrinkCategory.WHISKY, 40f, 46f, 40f, 30, 10f),
    WHISKY_BOURBON("波本威士忌", "🥃", DrinkCategory.WHISKY, 40f, 50f, 45f, 30, 11f),
    WHISKY_JAPANESE("日本威士忌", "🥃", DrinkCategory.WHISKY, 40f, 48f, 43f, 30, 10f),

    // ===== 白兰地类 =====
    BRANDY_COGNAC("干邑白兰地", "🥃", DrinkCategory.BRANDY, 40f, 45f, 40f, 30, 10f),
    BRANDY_ARMAGNAC("雅文邑", "🥃", DrinkCategory.BRANDY, 40f, 45f, 40f, 30, 10f),

    // ===== 伏特加/金酒 =====
    VODKA("伏特加", "🍸", DrinkCategory.VODKA, 35f, 50f, 40f, 30, 10f),
    GIN("金酒", "🍸", DrinkCategory.GIN, 37f, 50f, 40f, 30, 10f),

    // ===== 朗姆酒 =====
    RUM_WHITE("白朗姆", "🥥", DrinkCategory.RUM, 35f, 50f, 40f, 30, 10f),
    RUM_DARK("黑朗姆", "🥥", DrinkCategory.RUM, 35f, 50f, 40f, 30, 10f),

    // ===== 龙舌兰 =====
    TEQUILA("龙舌兰", "🌵", DrinkCategory.TEQUILA, 35f, 55f, 38f, 30, 9f),
    MEZCAL("梅斯卡尔", "🌵", DrinkCategory.TEQUILA, 40f, 55f, 45f, 30, 11f),

    // ===== 清酒 =====
    SAKE_REGULAR("清酒", "🍶", DrinkCategory.SAKE, 15f, 20f, 16f, 90, 11f),
    SAKE_DAIIGINJO("大吟酿", "🍶", DrinkCategory.SAKE, 15f, 20f, 16f, 90, 11f),
    SHOCHU("烧酎", "🍶", DrinkCategory.SAKE, 20f, 25f, 20f, 90, 14f),

    // ===== 韩国烧酒 =====
    SOJU("烧酒", "🍶", DrinkCategory.SOJU, 16f, 25f, 17f, 50, 7f),

    // ===== 鸡尾酒 =====
    COCKTAIL_SHORT("短饮鸡尾酒", "🍹", DrinkCategory.COCKTAIL, 10f, 25f, 15f, 60, 7f),
    COCKTAIL_LONG("长饮鸡尾酒", "🍹", DrinkCategory.COCKTAIL, 5f, 15f, 10f, 120, 10f),
    COCKTAIL_MOJITO("莫吉托", "🍹", DrinkCategory.COCKTAIL, 10f, 15f, 12f, 150, 14f),
    COCKTAIL_MARGARITA("玛格丽特", "🍹", DrinkCategory.COCKTAIL, 10f, 18f, 15f, 120, 14f),

    // ===== 利口酒 =====
    LIQUEUR("利口酒", "🍸", DrinkCategory.LIQUEUR, 15f, 40f, 25f, 30, 6f),

    // ===== 黄酒/米酒 =====
    HUANGJIU("黄酒", "🏺", DrinkCategory.HUANGJIU, 14f, 20f, 16f, 100, 13f),
    MIJIU("米酒", "🏺", DrinkCategory.HUANGJIU, 5f, 20f, 10f, 100, 8f),

    // ===== 其他 =====
    CIDER("苹果酒", "🍎", DrinkCategory.OTHER, 4f, 8f, 5f, 330, 13f),
    MEAD("蜂蜜酒", "🍯", DrinkCategory.OTHER, 8f, 20f, 12f, 150, 14f),
    ABSINTHE("苦艾酒", "🌿", DrinkCategory.OTHER, 45f, 74f, 55f, 30, 13f),
    VERMOUTH("味美思", "🍸", DrinkCategory.OTHER, 15f, 18f, 16f, 60, 8f),
    CUSTOM("自定义", "🥤", DrinkCategory.OTHER, 0f, 100f, 40f, 30, 10f);

    companion object {
        fun getByCategory(category: DrinkCategory): List<DrinkType> {
            return values().filter { it.category == category }
        }
    }
}

enum class DrinkCategory(val displayName: String, val emoji: String) {
    BEER("啤酒", "🍺"),
    WINE("葡萄酒", "🍷"),
    BAIJIU("白酒", "🥃"),
    WHISKY("威士忌", "🥃"),
    BRANDY("白兰地", "🥃"),
    VODKA("伏特加", "🍸"),
    GIN("金酒", "🍸"),
    RUM("朗姆酒", "🥥"),
    TEQUILA("龙舌兰", "🌵"),
    SAKE("清酒", "🍶"),
    SOJU("烧酒", "🍶"),
    COCKTAIL("鸡尾酒", "🍹"),
    LIQUEUR("利口酒", "🍸"),
    HUANGJIU("黄酒", "🏺"),
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
    val dailyAlcoholTargetGrams: Float = 20f,  // 每日纯酒精目标 g
    val reminderEnabled: Boolean = true,
    val reminderIntervalMinutes: Int = 120,
    val wakeUpHour: Int = 8,
    val sleepHour: Int = 23,
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
    val isOverLimit: Boolean get() = totalAlcoholGrams > 25f  // 超过中国膳食指南建议
    val progressPercent: Float get() = (totalAlcoholGrams / 20f * 100).coerceIn(0f, 150f)
}
