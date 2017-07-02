package com.emobi.convoy.pojo.findfriends;

import com.emobi.convoy.pojo.RegisterPOJO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 06-04-2017.
 */

public class FindFriendPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<RegisterPOJO> registerPOJO;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<RegisterPOJO> getRegisterPOJO() {
        return registerPOJO;
    }

    public void setRegisterPOJO(List<RegisterPOJO> registerPOJO) {
        this.registerPOJO = registerPOJO;
    }

    @Override
    public String toString() {
        return "FindFriendPOJO{" +
                "success='" + success + '\'' +
                ", registerPOJO=" + registerPOJO +
                '}';
    }
}
