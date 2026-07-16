package com.daigorian.epcltvapp

/**
 * 「放送中」キーワードグループ設定の1グループ分。
 * 設定画面のテキスト入力（1行1グループ）をパースして生成する。
 */
data class KeywordGroup(val name: String, val keywords: List<String>) {
    companion object {
        private val SEPARATOR_REGEX = Regex("[:：]")
        private val KEYWORD_DELIM_REGEX = Regex("[\\s,、]+")

        /**
         * 設定文字列（1行1グループ、「グループ名: キーワード1 キーワード2」形式）をパースする。
         * 区切りのない行・グループ名やキーワードが空になる行は無視する。
         */
        fun parse(pref: String): List<KeywordGroup> {
            return pref.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .mapNotNull { line ->
                    val parts = line.split(SEPARATOR_REGEX, limit = 2)
                    if (parts.size < 2) return@mapNotNull null
                    val name = parts[0].trim()
                    val keywords = parts[1].trim().split(KEYWORD_DELIM_REGEX).filter { it.isNotEmpty() }
                    if (name.isEmpty() || keywords.isEmpty()) null else KeywordGroup(name, keywords)
                }
                .toList()
        }
    }
}
