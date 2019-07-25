package com.andispy.andispy.lib;

public interface BackgroundWork<T> {
    T doInBackground() throws Exception;
}