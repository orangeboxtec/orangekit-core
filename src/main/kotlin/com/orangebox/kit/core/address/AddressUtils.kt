package com.orangebox.kit.core.address

import com.jayway.jsonpath.JsonPath
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Invocation
import jakarta.ws.rs.client.WebTarget
import jakarta.ws.rs.core.MediaType
import java.net.URLEncoder

class AddressUtils {

    fun geocodeAddress(address: AddressInfo) {
        var txtAddr: String? = if (address.address != null) {
            address.address
        } else {
            textualAddress(address)
        }
        txtAddr = URLEncoder.encode(txtAddr, "UTF-8")
        val client: Client = ClientBuilder.newClient()
        val webTarget: WebTarget = client.target("https://geocoder.api.here.com/6.2/geocode.json?app_id=iTblGnB2cI1S9wbvwBwU&app_code=8Co-JidyUE8VTYcZV8s8QA&searchtext=$txtAddr")
        val invocationBuilder: Invocation.Builder = webTarget.request(MediaType.APPLICATION_JSON)
        val json = invocationBuilder.get(String::class.java)
        val lat: Double = JsonPath.read(json, "$.Response.View[0].Result[0].Location.DisplayPosition.Latitude")
        address.latitude = lat
        val log: Double = JsonPath.read(json, "$.Response.View[0].Result[0].Location.DisplayPosition.Longitude")
        address.longitude = log
    }

    companion object {
        fun textualAddress(addressInfo: AddressInfo?): String {
            val en = StringBuilder()
            if (addressInfo != null) {
                if (addressInfo.street != null) {
                    en.append(addressInfo.street)
                }
                if (addressInfo.number != null) {
                    en.append(" n.")
                    en.append(addressInfo.number)
                    en.append(", ")
                }
                if (addressInfo.district != null) {
                    en.append(addressInfo.district)
                    en.append(", ")
                }
                if (addressInfo.city != null) {
                    en.append(addressInfo.city)
                }
                if (addressInfo.zipCode != null) {
                    en.append(" CEP:")
                    en.append(addressInfo.zipCode)
                }
            }
            return en.toString()
        }
    }
}