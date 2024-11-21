/*
 *  Copyright Gergely Nagy <greg@webhejj.hu>
 *
 *  Licensed under the Apache License, Version 2.0;
 *  you may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package hu.webhejj.commons.io.table;

import java.util.Collection;

public class CompareUtils {
    /**
     * @return true if specified value is null, empty string or containing only whitespace or empty collection
     */
    public static boolean isEmpty(Object o) {

        if (o == null) {
            return true;
        }

        if (o instanceof String) {
            return ((String) o).isBlank();

        } else if (o instanceof Collection<?>) {
            return ((Collection<?>) o).size() == 0;
        }

        return false;
    }
}
