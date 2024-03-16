package org.nocturnum.batch;

import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    public void toLowerCase() throws Exception {
        String str = "#{ID}, #{NAME}, #{EMAIL}, #{JOIN_DATE}, #{LAST_ACCESS_DATE}";
        System.out.println(str.toLowerCase());
    }

}
