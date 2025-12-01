{
  pkgs,
  lib,
  config,
  inputs,
  ...
}:

{
  languages.java = {
    jdk.package = pkgs.temurin-bin-25;
    enable = true;
    gradle.enable = true;
    gradle.package = pkgs.gradle_9;
  };

  languages.python.enable = true;
}
