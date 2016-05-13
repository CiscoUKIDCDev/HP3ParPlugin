# HP 3PAR UCS Director Plugin
This is an Open Automation plug-in which adds 3PAR support to Cisco UCS Director.

You can **[download the latest releases here](https://github.com/CiscoUKIDCDev/HP3ParPlugin/releases)**.

The plugin is being actively developed on an ad-hoc basis. Upcoming features are documented over in the [issues tab](https://github.com/CiscoUKIDCDev/HP3ParPlugin/issues). You can also create requests there if you'd like new features or have found bugs.

The project is released under a generous [MIT-style license](https://github.com/CiscoUKIDCDev/HP3ParPlugin/blob/master/LICENSE) which means you can download it, change it and even charge for it provided the copyright is maintained. I'd request kindly that you send back any bugfixes as pull requests, but you have no obligation to do so.

For installation instructions see the bottom of this page.

## Warning
This plugin has never been tested in a production environment before!

There is no support or warranty - implied or otherwise. If you need new features or bugfixes, ask nicely in the [issues tab](https://github.com/CiscoUKIDCDev/HP3ParPlugin/issues). The code is there too, so you can fix it yourself (and ideally send those back as a pull request!).

You should back-up your UCS Director database before using this plugin and test any new releases before deploying.

## Screenshots
![Converged view screenshot](https://matt.fragilegeek.com/ucsd/3PAR-Plugin-summary.png)

![Workflow task screenshot](https://matt.fragilegeek.com/ucsd/3PAR-Plugin-Workflow.png)

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
* Inventory collection and database caching
* Full support to create/delete VLUNs and Hosts
* Host sets

### Near-term roadmap
* Volume sets
* More tasks (need suggestions - use the [issues tab](https://github.com/CiscoUKIDCDev/HP3ParPlugin/issues) to do so)

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
