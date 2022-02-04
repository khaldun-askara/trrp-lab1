package com.github.scribejava.apis.examples;

import java.util.ArrayList;

public class DBFolder {
    public  String tag;
    public String name;
    public String id;
    public String client_modified;
    public String server_modified;
    public String rev;
    public int size;
    public String path_lower;
    public String path_display;
    public Sharing_info sharing_info;
    public boolean is_downloadable;
    public ArrayList <Property_group> property_groups = new ArrayList<Property_group>();
    public boolean has_explicit_shared_members;
    public String content_hash;
    public File_lock_info file_lock_info;

    public String getName() {return name; }
    public String toString() {return name;}
}