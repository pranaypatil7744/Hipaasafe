package com.hipaasafe.presentation.help.model

data class HelpModel(
    var icon: Int? = 0,
    var label: String = "",
    var data: String = "",
    var type: HelpItemType
)

enum class HelpItemType(val value: Int) {
    HEADER(10),
    EMAIL(20),
    CALL(30)
}
