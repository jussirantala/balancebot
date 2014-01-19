balancebot
==========
![alt text](https://ip.bitcointalk.org/?u=http%3A%2F%2Foi39.tinypic.com%2F23uewr9.jpg&t=536&c=Q9tkB0WHnTRlVw )<br>
This bot will get your Mt.Gox acount balance and BTC price every X minutes and store them in CSV file. The CSV file can be used to make graph with Excel to compare how well you have traded VS how much bitcoin has risen if you convert them to percents. 
<br><br>
How to use:<br>
1. Download the bot from https://github.com/jussirantala/balancebot/blob/master/balancebot_release.zip?raw=true<br>
2. Extract files<br>
3. Open goxbot.cfg with a text editor<br>
4. Log in to mt.gox website and create new API key for this bot<br>
5. Enter API key details to the config file you opened<br>
6. Change interval to how many minutes you want the bot to wait between getting and saving data<br>
7. Open command line or terminal depending on your operating system and navigate to the folder of extracted files<br>
8. Start the bot with this command: java -jar balancebot.jar<br>
9. Let it run 24/7 (I run it at my Linux server). If you are using linux, you can make it to run on background by starting it like this: screen java -jar balancebot.jar
<br><br>
10. Donate if you like it: <br>
    BTC:   158UQ2niJ6eVQgPsfJaS791s9hBruVWBUU<br>
    LTC:   LauBymAt8Kkz9CLmZ8QA5h8rfV6DACfVSS<br>
    Doge:  DQsANjjfnbBDR42S9NrWfcswRXiFDC9xr1<br>
    PPC:   PF7y9TCDwBGcZkaRGTFPpN3SNM7q69i3Kc

Example chart with 60 min interval used: 
![alt text](https://ip.bitcointalk.org/?u=http%3A%2F%2Foi40.tinypic.com%2F16lg106.jpg&t=536&c=p3YtClZeHWJ34A )
