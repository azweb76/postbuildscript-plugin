package org.jenkinsci.plugins.postbuildscript;

import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixRun;
import hudson.model.BuildListener;

import java.io.IOException;

public class ConfigurableMatrixAggregator extends MatrixAggregator {

    private final Processor processor;

    private final ExecuteOn executeOn;

    public ConfigurableMatrixAggregator(
        MatrixBuild build,
        Launcher launcher,
        BuildListener listener,
        ProcessorFactory processorFactory,
        ExecuteOn executeOn
    ) {
        super(build, launcher, listener);
        processor = processorFactory.create(build, launcher, listener);
        this.executeOn = executeOn;
    }

    @Override
    public boolean endRun(MatrixRun run) throws InterruptedException, IOException {
        listener.getLogger().println(run);
        return super.endRun(run);
    }

    @Override
    public boolean endBuild() throws InterruptedException, IOException {
        return !executeOn.matrix() || processor.process();
    }
}
