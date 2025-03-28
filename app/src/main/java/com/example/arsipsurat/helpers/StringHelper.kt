package com.example.arsipsurat.helpers

import java.util.Locale

object StringHelper {
    fun removeExtraSpaces(text: String?): String {
        return text?.trim()?.replace("\\s+".toRegex(), " ") ?: ""
    }

    fun capitalizeFirstLetter(text: String?): String {
        return text?.capitalize(Locale.ROOT) ?: ""
    }
}
