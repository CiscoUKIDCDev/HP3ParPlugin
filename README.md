# HP 3PAR UCS Director Plugin
This intends to be a comprehensive UCS Director plugin for HP 3PAR storage blades.

It is currently in early alpha, it is unsupported and used at your own risk. If you'd like to give it a go, the latest copy is always [available here](https://github.com/CiscoUKIDCDev/HP3ParPlugin/blob/master/Open_Automation/HP3Par-plugin.zip?raw=true).

![Converged view screenshot](https://matt.fragilegeek.com/ucsd-3par-summary.png)

![Account screenshot](https://matt.fragilegeek.com/ucsd-3par-account.png)

## Features

### Implemented
* Ability to add a 3PAR physical storage account (and converged stack based on it)
* Converged/Physical view tab (list of volumes, CPGs and some graphs)

### Near-term roadmap
* Basic tasks (create/delete volume)
* Better documentation

## Using
You need to enable the WSAPI on your 3PAR array. Further instructions coming later as/when it becomes more stable. I don't recommend this for production use today.

## Credits
Most of this was lovingly ripped off (and inspired by) Russ Whitear's Nimble plugin (he was super helpful!)

https://github.com/rwhitear42/
