# sonar-esql-plugin [![Build Status](https://github.com/EXXETA/sonar-esql-plugin/actions/workflows/build.yml/badge.svg)](https://travis-ci.org/EXXETA/sonar-esql-plugin) [![codecov](https://codecov.io/gh/EXXETA/sonar-esql-plugin/branch/develop/graph/badge.svg)](https://codecov.io/gh/EXXETA/sonar-esql-plugin) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=EXXETA_sonar-esql-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=EXXETA_sonar-esql-plugin)

This open source plugin can be used to analyze the ESQL-sourcecode of IBM App Connect Enterprise / IBM Websphere Message Broker / IBM Integration Bus projects. 

## Features
* \>75 rules
* Support for Broker 7, 8, 9, 10, 11, 12
* Metrics (complexity, number of lines, ...)
* Import of traces as coverage reports

## Installation

1. Install SonarQube (https://docs.sonarqube.org/display/SONAR/Get+Started+in+Two+Minutes)
2. Place the latest esql-plugin-<version>.jar from esql-plugin/target to the plugin directory of SonarQube(\extensions\plugins)

## Requirements

- SonarQube 7.9

## Build

1. mvn clean install

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request

## History

- 3.4.0 - New coverage format, bugfixes, Sonar 9.4
- 3.3.0 - Upgrade to SonarQube 8.9
- 3.0.0 - Bugfixes, upgrade to sonar SonarQube 7.9
- 2.3.0 - Additional rules, upgrade to SonarQube 6.7, copy paste detector
- 2.2.0 - Code coverage analysis
- 2.1.0 - A few bugfixes and a lot more checks/rules
- 2.0.0 - complete refactoring and upgrade to SonarQube 5.6
- 1.1.0 - Fix issues #7(https://github.com/EXXETA/sonar-esql-plugin/issues/7) and #8(https://github.com/EXXETA/sonar-esql-plugin/issues/8)
- 1.0.1 - Fix issue #5(https://github.com/EXXETA/sonar-esql-plugin/issues/5)
- 1.0   - Initial release


## Credits

- [EXXETA AG](http://exxeta.com)
- See [Contributors](https://www.github.com/EXXETA/sonar-esql-plugin/graphs/contributors)

## License

Apache License v2.0
