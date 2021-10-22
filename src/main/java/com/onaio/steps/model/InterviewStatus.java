/*
 * Copyright 2016. World Health Organization
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onaio.steps.model;

import java.util.HashMap;

public enum InterviewStatus {
    EMPTY_HOUSEHOLD(0),
    SELECTION_NOT_DONE(4),
    INCOMPLETE(1),
    NOT_DONE(2),
    DONE(5),
    DEFERRED(3),
    REFUSED(8),
    INCOMPLETE_REFUSED(9),
    SUBMITTED(6),
    NOT_REACHABLE(7);
    private int orderWeight;
    private static HashMap<Integer, Integer> statusToWeight;

    InterviewStatus(int orderWeight){

        this.orderWeight = orderWeight;
    }
    public Integer getOrderWeight(){
        return getStatusToOrderWeight();
    }

    public Integer getStatusToOrderWeight() {
        if (statusToWeight == null) {
            statusToWeight = new HashMap<>();
            statusToWeight.put(0, 0);
            statusToWeight.put(1, 1);
            statusToWeight.put(2, 2);
            statusToWeight.put(3, 3);
            statusToWeight.put(4, 4);
            statusToWeight.put(5, 5);
            statusToWeight.put(6, 6);
            statusToWeight.put(7, 7);
            statusToWeight.put(8, 9);
            statusToWeight.put(9, 8);
        }
        return statusToWeight.get(orderWeight);
    }
}
