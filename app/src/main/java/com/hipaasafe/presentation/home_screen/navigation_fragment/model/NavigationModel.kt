package com.hipaasafe.presentation.home_screen.navigation_fragment.model

data class NavigationModel(
    var title:String? ="",
    var icon:Int? = null,
    var navItemType: NavItemType
)
enum class NavItemType(val value:Int){
    ITEM_PROFILE(1),
    ITEM_MENU(2),
    ITEM_SIGN_OUT(3),
    DIVIDER(4)
}
