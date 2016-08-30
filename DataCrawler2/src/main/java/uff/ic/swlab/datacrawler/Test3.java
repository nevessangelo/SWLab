package uff.ic.swlab.datacrawler;

import uff.ic.swlab.utils.Resource;

public class Test3 {

    public static void main(String[] args) {

        String[] urls = {"http://acm.rkbexplorer.com/id/999",
            "http://acm.rkbexplorer.com/id/998",
            "http://acm.rkbexplorer2.com/id/999",
            "http://acm.rkbexplorer2.com/id/998",
            "http://acm.rkbexplorer2.com/id/997"};

        System.out.println(Resource.getAuthority(urls));
    }
}
