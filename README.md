# HP 3PAR UCS Director Plugin
This intends to be a comprehensive UCS Director plugin for HP 3PAR storage systems.

It is currently in early alpha, it is unsupported and used at your own risk. If you'd like to give it a go, you can find the [latest releases here](https://github.com/CiscoUKIDCDev/HP3ParPlugin/releases).

Alternatively, you can check out the project and import it in to the Eclipse IDE and build it yourself.

## Warning
This plugin is considered unstable - your warranty is now void!

In particular, you should remove all 3PAR accounts from your UCS Director installation before upgrading and then re-add them later. Things change between builds that might leave you with a headache if you don't do this! Once the release 1.x.x happens this won't be a requirement any more.

It is **not** recommended to run this plugin in production - it has never been tested beyond a basic lab environment.

## Screenshots
![Converged view screenshot](https://matt.fragilegeek.com/ucsd/ucsd-3par-summary)

![Account screenshot](https://matt.fragilegeek.com/ucsd/ucsd-3par-account)

![Task Screenshot](https://matt.fragilegeek.com/ucsd/ucsd-3par-workflow-create)

![CPG Selector Screenshot](https://matt.fragilegeek.com/ucsd/ucsd-3par-cpg-selector)


## Features

### Implemented
* Ability to add a 3PAR physical storage account (and converged stack based on it)
* Converged/Physical view tab (list of volumes, CPGs and some graphs)
* Form lists to pick 3PAR accounts, CPGs and Volumes
* Action buttons on the volume form list to create/delete
* Create volume task (may be buggy)
* Full Javadoc in source code

### Near-term roadmap
* More tasks (delete volumes, deal with CPGs etc)
* Drilldown reports (e.g. double-click CPG to see its volumes)

## Using
### Configuring the WSAPI on your 3PAR array
You need to enable the WSAPI on your 3PAR array. The official instructions are [available here](http://h20564.www2.hpe.com/hpsc/doc/public/display?docId=c03606339).

In a nutshell, you need to ssh to your 3PAR controller and issue the following command:
```
 startwsapi
```
You can check the status by doing this:
```
showwsapi
```
The plugin supports both http and https and the latter is strongly recommended.

### Installing in UCS Director

1. Under **Administration -> Open Automation** upload the plugin file (HP3Par-plugin.zip). Select it and click **enable**.
2. From the CLI (typically ssh logged in as shelladmin) select **3** to stop services and then **4** to start them.
3. You can then add a 3PAR account under a Generic, FlexPod or 3PAR pod

Further instructions and better documentation will be out as/when it becomes more stable.

## Credits
Most of this was lovingly ripped off (and inspired by) Russ Whitear's Nimble plugin (he was super helpful!)

https://github.com/rwhitear42/
