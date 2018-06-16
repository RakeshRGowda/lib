package com.example.http;

/*
USER        Date            Version             Changes
Rakesh      13-06-2018     Initial Draft       No changes.
*/


public interface HttpTaskCompleteListener<String> {
    void onSuccess(String success);
    void onFailure(String failure);
}
