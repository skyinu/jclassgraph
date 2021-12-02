package com.skyinu.classanalyze;

public class JClassUsageFinderEntry {

    /**
     * how to debug in ide
     * https://stackoverflow.com/questions/2066307/how-do-you-input-command-line-arguments-in-intellij-idea
     * @param args
     */
    public static void main(String[] args) {
        new JClassUsageFinder().findUsage(args);
    }
}
