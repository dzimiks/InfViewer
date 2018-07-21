/***********************************************************************
 * Module:  MainObserver.java
 * Author:  dzimiks
 * Purpose: Defines the Interface MainObserver
 ***********************************************************************/

package observer;

import java.util.*;

public interface MainObserver {
   void update(NotificationObserver notification, Object obj);

}