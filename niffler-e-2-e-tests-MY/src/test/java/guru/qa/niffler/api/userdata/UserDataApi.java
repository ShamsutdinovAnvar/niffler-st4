package guru.qa.niffler.api.userdata;

import guru.qa.niffler.model.userdata.FriendJson;
import guru.qa.niffler.model.userdata.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserDataApi {

    @GET("/friends")
    Call<List<UserJson>> getUserFriends(@Query("username") String username,
                                        @Query("includePending") boolean includePending
    );

    @GET("/invitations")
    Call<List<UserJson>> getUserInvitations(@Query("username") String username);

    @POST("/acceptInvitation")
    Call<List<UserJson>> acceptInvitations(@Query("username") String username,
                                           @Body() FriendJson invitation
    );

    @POST("/declineInvitation")
    Call<List<UserJson>> declineInvitation(@Query("username") String username,
                                           @Body() FriendJson invitation
    );

    @POST("/addFriend")
    Call<List<UserJson>> addFriend(@Query("username") String username,
                                   @Body() FriendJson friend
    );

    @DELETE("/removeFriend")
    Call<List<UserJson>> deleteFriend(@Query("username") String username,
                                      @Query("friendUsername") String friendUsername
    );

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson user);


    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") String username);


    @GET("/allUsers")
    Call<List<UserJson>> getAllUsers(@Query("username") String username);
}