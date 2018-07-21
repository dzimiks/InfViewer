/***********************************************************************
 * Module:  ObserverList.java
 * Author:  dzimiks
 * Purpose: Defines the Class ObserverList
 ***********************************************************************/

package observer;

import java.util.*;

public class ObserverList {
   private ArrayList<MainObserver> observers;
   
   public java.util.Collection<MainObserver> mainObserver;
   
   public void notifyObservers(NotificationObserver notification, Object obj) {
      // TODO: implement
   }
   
   public void addObserver(MainObserver observer) {
      // TODO: implement
   }
   
   
   public java.util.Collection<MainObserver> getMainObserver() {
      if (mainObserver == null)
         mainObserver = new java.util.HashSet<MainObserver>();
      return mainObserver;
   }
   
   public java.util.Iterator getIteratorMainObserver() {
      if (mainObserver == null)
         mainObserver = new java.util.HashSet<MainObserver>();
      return mainObserver.iterator();
   }
   
   public void setMainObserver(java.util.Collection<MainObserver> newMainObserver) {
      removeAllMainObserver();
      for (java.util.Iterator iter = newMainObserver.iterator(); iter.hasNext();)
         addMainObserver((MainObserver)iter.next());
   }
   
   public void addMainObserver(MainObserver newMainObserver) {
      if (newMainObserver == null)
         return;
      if (this.mainObserver == null)
         this.mainObserver = new java.util.HashSet<MainObserver>();
      if (!this.mainObserver.contains(newMainObserver))
         this.mainObserver.add(newMainObserver);
   }
   
   public void removeMainObserver(MainObserver oldMainObserver) {
      if (oldMainObserver == null)
         return;
      if (this.mainObserver != null)
         if (this.mainObserver.contains(oldMainObserver))
            this.mainObserver.remove(oldMainObserver);
   }
   
   public void removeAllMainObserver() {
      if (mainObserver != null)
         mainObserver.clear();
   }

}