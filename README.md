# HP 3PAR UCS Director Plugin
This is an Open Automation plug-in which adds 3PAR support to Cisco UCS Director.

You can **[download the latest releases here](https://github.com/CiscoUKIDCDev/HP3ParPlugin/releases)**.

Right now it's in maintenence mode. It's considered alpha, but no new features will be added directly without community request. If you wish to request a feature, use the [issues tab](https://github.com/CiscoUKIDCDev/HP3ParPlugin/issues) and create a new issue with your request.

For installation instructions see the bottom of this page.

This plugin is licensed under an [MIT-style license](https://github.com/CiscoUKIDCDev/HP3ParPlugin/blob/master/LICENSE).

## Warning
This plugin has never been tested in a production environment before!

There is no support or warranty. It is a community plugin. If you need new features or bugfixes, ask nicely. The code is there too, so you can fix it yourself (and ideally send those back as a pull request!).

## Screenshots
![Converged view screenshot](https://matt.fragilegeek.com/ucsd/3par-plugin-summary-page)

![Workflow task screenshot](https://matt.fragilegeek.com/ucsd/3par-plugin-workflow-tasks)

For more screenshots see the [screenshots page](screenshots.md).

## Features

### Implemented
* Ability to add a 3PAR physical storage account (and converged stack based on it)
* Converged/Physical view tab (list of volumes, CPGs and some graphs)
* Drilldown reports (double-click on CPGs and Volumes to see more information and graphs)
* Form lists to pick 3PAR accounts, CPGs and Volumes
* Action buttons on the volume form list to create/delete and to perform snapshots & copies
* Create, Delete, Snapshot and Copy volume tasks
* Full Javadoc in source code

### Near-term roadmap
* More tasks (need suggestions)

Additionally, this plugin is mostly trying to be a clean plugin implementation. 

That means there is almost no "dummy" SDK code in here (except a few boilerplate cases). If you wish to hack on this you will need to download the SDK from Cisco's website, especially to add new features.

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
