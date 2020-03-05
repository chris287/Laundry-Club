package com.example.lc;

import java.util.List;

public interface firebaseListner {
    void onFireBaseLoadSuccess(List<UserModule> userModuleList);
    void onFireBaseLoadFailed(String message);
}
