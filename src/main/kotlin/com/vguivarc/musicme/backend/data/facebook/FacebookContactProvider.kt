package com.vguivarc.musicme.backend.data.facebook

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.contact.IContactFacebookProvider
import org.springframework.http.MediaType
import org.springframework.social.connect.Connection
import org.springframework.social.facebook.api.*
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.stereotype.Service
import org.springframework.test.annotation.Timed
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@Service
class FacebookContactProvider : IContactFacebookProvider {
    /*
    @RequestMapping(value = ["/fbfriends"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Timed
    fun GetFriends(connection: Connection<*>?): List<User?>? {
        print("REST request to get all Friends")
        if (connection == null) {
            print("Cannot create social user because connection is null")
            throw IllegalArgumentException("Connection cannot be null")
        }
        print("Got connection")
        val permissions: List<Permission> = facebook!!.userOperations().userPermissions
        print("Permission size: " + permissions.size + " Permission: " + permissions)
        for (perm in permissions) {
            print(perm.getName().toString() + " " + perm.getStatus())
        }
        val friends: List<User?> = facebook.friendOperations().friendProfiles
        print("Friends size: " + friends.size + " Friends:" + friends.toString())
        val feed: PagedList<Post> = facebook.feedOperations().feed
        print("Feed size: " + feed.size + " Feed: " + feed.toString())
        val taggable = facebook.friendOperations().taggableFriends
        print("Taggable size: " + friends.size + " Taggable:" + friends.toString())
        print("Ho Ho Ho")
        return friends
    }
    */


    override fun getContactFacebookIdList(socialAuthToken: String): List<String> {
        val facebook: Facebook = FacebookTemplate(socialAuthToken)
        return facebook.friendOperations().friendIds
       /* val listFacebookId = mutableListOf<String>()
        for (id in friendsIds) {
            val user = facebook.userOperations().getUser
            listFacebookId.add(user.id)
            println(user.email)
            println(user.firstName)
        }
        return listFacebookId*/
/*
        GetFriends(Connection)
        val fb = Facebook.friendOperations().friendIds.toList()
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { _, _ ->
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/$facebookId/friends",
                null,
                HttpMethod.GET
            ) { response2 ->
                try {
                    val rawName = response2.jsonObject.getJSONArray("data")
                    val listIdFacebook = mutableListOf<String>()
                    for (i in 0 until rawName.length()) {
                        val jsonFacebook = rawName.getJSONObject(i)
                        listIdFacebook.add(jsonFacebook.getString("id"))
                    }
                    contactApi.getContactFacebook(listIdFacebook)
                    _contactFacebookList.value = ContactService.ContactFacebookListState(
                        list = contactApi.getContactFacebook(listIdFacebook).blockingGet().contacts
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    _contactFacebookList.value =
                        ContactService.ContactFacebookListState(error = e)
                }
            }.executeAsync()
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()*/
    }

}