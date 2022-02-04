package com.github.scribejava.apis.examples;

import com.github.scribejava.core.model.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Dropbox {
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private final String urlPrefix = "https://api.dropboxapi.com/2/files/";

    private final RequestExecutor requestExecutor;

    Dropbox() throws InterruptedException, ExecutionException, URISyntaxException, IOException{
        requestExecutor = new RequestExecutor();
    }

    Dropbox(String refreshToken) throws InterruptedException, ExecutionException, URISyntaxException, IOException{
        requestExecutor = new RequestExecutor(refreshToken);
    }

    // create
    public void createFolder(String folderName){
        final OAuthRequest request = new OAuthRequest(Verb.POST, urlPrefix+"create_folder_v2/");
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(new Gson().toJson(new ListFolderArg("/dirs" + "/" + folderName)));
        try {
            requestExecutor.execute(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //read
    List<DBFolder> getFolders() {
        final OAuthRequest request = new OAuthRequest(Verb.POST, urlPrefix+"list_folder/");
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(new Gson().toJson(new ListFolderArg("/dirs")));
        try {
            Response response = requestExecutor.execute(request);
            Gson g = new Gson();
            list_folderRETURNS returns = g.fromJson(response.getBody(), list_folderRETURNS.class);
            return returns.entries;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    // update
    public void updateFolder(String oldfolderName, String newfolderName){
        final OAuthRequest request = new OAuthRequest(Verb.POST, urlPrefix+"move/");
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(new Gson().toJson(new MoveArg("/dirs" + "/" + oldfolderName, "/dirs" + "/" + newfolderName)));
        try {
            requestExecutor.execute(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    // delete
    public void deleteFolder(String folderName){
        final OAuthRequest request = new OAuthRequest(Verb.POST, urlPrefix+"delete/");
        request.addHeader(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE);
        request.setPayload(new Gson().toJson(new ListFolderArg("/dirs" + "/" + folderName)));
        try {
            requestExecutor.execute(request);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getrefToken(){
        try{
            return requestExecutor.getRefreshToken();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

