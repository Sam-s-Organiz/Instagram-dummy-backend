package com.Instagram.Dummy.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestParameters {
    private long start = 0;
    private long end = 10;

    public long getPageSize() {
        return end - start;
    }

    public int getPageNumber() {
        return (int) (start / getPageSize());  // Calculate the page number.
    }

}

