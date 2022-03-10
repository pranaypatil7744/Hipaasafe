package com.hipaasafe.domain.model.static_details

data class GetStaticDetailsResponseModel(
    var success:Boolean? = null,
    var message:String? = "",
    var data:ArrayList<StaticDateModel> = ArrayList()
)

data class StaticDateModel(
    var id:Int? = 0,
    var key:String? = "",
    var value:String? = ""
)
