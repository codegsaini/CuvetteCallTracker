# <p align="center">Cuvette Call Tracker</p>
### <p align="center">Documentation</p>
---
### ◉ Introduction
- Track call logs and show different statistics i.e. total calls, number of missed calls, number of incoming calls, etc.
- Record phone calls automatically when enabled from settings with proper consent from the user.

### ◉ Technical Brief
- Language: Kotlin
- UI: Jetpack Compose.
- Follows MVVM design pattern

### ◉ Objectives achieved 
(_As mentioned in the assignment manual_)

- [x] Total calls
- [x] Number of incoming calls
- [x] Number of outgoing calls
- [x] Number of missed calls
- [x] Duration of each call
- [x] Total call duration
- [x] Phone numbers involved in the calls

### ◉ Call log
To detect any call-related activity, `BroadcastReceiver` is registered to listen to the changes in phone state with the help of the `READ_PHONE_STATE` intent filter.
In the `onReceive()` method we can get some extras to help log the call info such as -
- `EXTRA_PHONE_STATE` which will give `STATE_IDLE`, `STATE_RINGING` and `STATE_IDLE` values
- `EXTRA_INCOMING_NUMBER` which will give the number of incoming/outgoing call
Here, one thing to notice is that the `BroadCastReceiver` does not give any information whether the call is incoming or outgoing.

**But the incoming/outgoing conclusion can be derived with the help of below flow chart -**

![CallStatus_FlowChart](https://github.com/user-attachments/assets/aa3b940f-a47c-46ce-97ef-e30f99b42835)

### ◉ Call recording
Recording a phone call is very much connected to the privacy concern of the user if misused. Hence android has many limitations/restrictions regarding the ability to record calls by third-party apps. One such limitation is that from android 9 (API 28) app can't record any audio input from device while in background as per [android documentation](https://developer.android.com/media/platform/sharing-audio-input#:~:text=One%20more%20change,at%20the%20time.) which states that -
> One more change was added in Android 9: only apps running in the foreground (or a foreground service) could capture the audio input. When an app without a foreground service or foreground UI component started to capture, the app continued running but received silence, even if it was the only app capturing audio at the time.

Solution to this problem is [`ForegroundService`](https://developer.android.com/develop/background-work/services/fgs)

But the problem is still not solved, which I will cover in _Known Issues_ section.
### ◉ Known Bugs
Although the app is designed to record call but from

### User Interface

 - Home page with list of call logs and all data stats
<img src="https://github.com/user-attachments/assets/a0e55a3c-ce0c-4abf-807e-49cd3712db23" width="300" />

---

- When the user click on `Enable Recording` button, a consent popup will be shown
<img src="https://github.com/user-attachments/assets/4cf27a97-9112-417d-873c-f309a47ff4a1" width="300" />

---

- This is the detail page of particular number, here voice recording play/pause button is also available
<img src="https://github.com/user-attachments/assets/57e823f9-81bc-4bd3-aca0-45aba1e39377" width="300" />
