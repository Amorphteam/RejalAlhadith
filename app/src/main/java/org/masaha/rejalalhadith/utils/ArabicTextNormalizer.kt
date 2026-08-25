package org.masaha.rejalalhadith.utils

object ArabicTextNormalizer {

    // Map letter variants to a common form (أ/إ/آ/ٱ→ا, ة→ه, …) and strip tashkeel/tatweel.
    private val charMap: Map<Char, Char?> = buildMap {
        put('أ', 'ا')
        put('إ', 'ا')
        put('آ', 'ا')
        put('ٱ', 'ا')
        put('ة', 'ه')
        put('ى', 'ي')
        put('ئ', 'ي')
        put('ؤ', 'و')
        put('ک', 'ك')
        put('ی', 'ي')
        put('ـ', null) // tatweel
        for (code in 0x064B..0x065F) {
            put(code.toChar(), null)
        }
        put('\u0670', null) // dagger alef
    }

    fun normalize(text: String): String {
        if (text.isEmpty()) {
            return text
        }

        val builder = StringBuilder(text.length)
        for (char in text) {
            when {
                charMap.containsKey(char) -> {
                    val mapped = charMap[char]
                    if (mapped != null) {
                        builder.append(mapped)
                    }
                }
                else -> builder.append(char)
            }
        }
        return builder.toString()
    }

    /**
     * Builds a SQLite expression that normalizes [column] the same way as [normalize].
     * Uses CHAR(codepoint) so the SQL stays ASCII-safe.
     */
    fun sqlNormalizeExpression(column: String): String {
        var expression = column
        val replacements = listOf(
                0x0623 to 0x0627, // أ → ا
                0x0625 to 0x0627, // إ → ا
                0x0622 to 0x0627, // آ → ا
                0x0671 to 0x0627, // ٱ → ا
                0x0629 to 0x0647, // ة → ه
                0x0649 to 0x064A, // ى → ي
                0x0626 to 0x064A, // ئ → ي
                0x0624 to 0x0648, // ؤ → و
                0x06A9 to 0x0643, // ک → ك
                0x06CC to 0x064A  // ی → ي
        )

        for ((from, to) in replacements) {
            expression = "REPLACE($expression,CHAR($from),CHAR($to))"
        }

        // tatweel + tashkeel range + dagger alef
        val strip = listOf(0x0640) + (0x064B..0x065F) + listOf(0x0670)
        for (code in strip) {
            expression = "REPLACE($expression,CHAR($code),'')"
        }

        return expression
    }

    fun escapeSqlLiteral(value: String): String {
        return value.replace("'", "''")
    }
}
