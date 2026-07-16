package com.daigorian.epcltvapp

/**
 * 「放送中」キーワードグループ設定の1グループ分。
 * 設定画面のテキスト入力（1行1グループ）をパースして生成する。
 */
data class KeywordGroup(
    val name: String,
    val keywords: List<String>,
    val excludeKeywords: List<String> = emptyList()
) {
    companion object {
        private val SEPARATOR_REGEX = Regex("[:：]")
        private val KEYWORD_DELIM_REGEX = Regex("[\\s,、]+")

        // TV のリモコン/ソフトウェアキーボードでは半角ハイフン "-" のつもりで
        // 全角ハイフン「－」や長音記号「ー」、各種ダッシュが入力されることがあるため、
        // 除外プレフィックスとして見た目が紛らわしい文字を広めに許容する。
        private val EXCLUDE_PREFIX_CHARS = setOf('-', '－', 'ー', '‐', '−', '–', '—', '─')

        /**
         * 設定文字列（1行1グループ、「グループ名: キーワード1 キーワード2」形式）をパースする。
         * キーワードの先頭に "-" のようなダッシュ記号を付けると除外キーワードになる（例: "-プロ野球"）。
         * 区切りのない行・グループ名や含むキーワードが空になる行は無視する。
         */
        fun parse(pref: String): List<KeywordGroup> {
            return pref.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .mapNotNull { line ->
                    val parts = line.split(SEPARATOR_REGEX, limit = 2)
                    if (parts.size < 2) return@mapNotNull null
                    val name = parts[0].trim()
                    val tokens = parts[1].trim().split(KEYWORD_DELIM_REGEX).filter { it.isNotEmpty() }

                    val keywords = mutableListOf<String>()
                    val excludeKeywords = mutableListOf<String>()
                    tokens.forEach { token ->
                        if (token.first() in EXCLUDE_PREFIX_CHARS) {
                            token.drop(1).takeIf { it.isNotEmpty() }?.let { excludeKeywords.add(it) }
                        } else {
                            keywords.add(token)
                        }
                    }

                    if (name.isEmpty() || keywords.isEmpty()) null else KeywordGroup(name, keywords, excludeKeywords)
                }
                .toList()
        }
    }
}
