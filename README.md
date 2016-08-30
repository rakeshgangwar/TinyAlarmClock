# TinyAlarmClock

Target SDK Version - 23, <br>
Minimum SDK Version - 21

# Functionality
- Digital Clock and Date displayed
- Addition of multiple one time alarms using Floating Action Button
- List of Alarms displayed in ListView below the clock
- To delete an alarm, tap the item on the listView
- Alarm is removed from the memory once the alarm is delivered

#Known Issues
- Alarm doesn't comes up at the exact time, and is delayed by several seconds. But it occurs inside that particular minute.
<strong>Reason</strong> - In newer APIs alarm delivery is inexact and OS shifts alarms in order to minimize wakeups and battery use.
Although I have used new APIs provided for strict delivery guarantee, the issue still persists.

- ListView contains time in 24 hour format. <br>
<strong>Reason</strong> - For now I am using only one column in DB i.e alarmTime, from the DB all the values are populated as it is on the ListView and I have used the values in ListView for updating and deleting the values in DB, because of which I couldn't convert the time in 12 hour format. Although I am still trying to figure out the feasible solution for this.

- Code hasn't been refactored properly yet, since I was trying to deliver the running application in the minimum possible time.
