/***********************************************************************
 * Module:  Observable.java
 * Author:  dzimiks
 * Purpose: Defines the Interface Observable
 ***********************************************************************/

package observer;

import java.util.*;

public interface Observable {
   void addObserver(MainObserver observer);

}