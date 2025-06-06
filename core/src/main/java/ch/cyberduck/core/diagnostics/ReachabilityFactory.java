package ch.cyberduck.core.diagnostics;

/*
 * Copyright (c) 2002-2009 David Kocher. All rights reserved.
 *
 * http://cyberduck.ch/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Bug fixes, suggestions and comments should be sent to:
 * dkocher@cyberduck.ch
 */

import ch.cyberduck.core.Factory;
import ch.cyberduck.core.Host;
import ch.cyberduck.core.JumpHostConfiguratorFactory;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.proxy.ProxyFactory;
import ch.cyberduck.core.proxy.ProxyFinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReachabilityFactory extends Factory<Reachability> {
    private static final Logger log = LogManager.getLogger(ReachabilityFactory.class);

    private ReachabilityFactory() {
        super("factory.reachability.class");
    }

    public static Reachability get() {
        return new ProtocolAwareReachability(new ReachabilityFactory().create());
    }

    private static final class ProtocolAwareReachability implements Reachability {
        private final Reachability monitor;

        public ProtocolAwareReachability(final Reachability monitor) {
            this.monitor = monitor;
        }

        @Override
        public void test(final Host bookmark) throws BackgroundException {
            final ProxyFinder proxy = ProxyFactory.get();
            switch(bookmark.getProtocol().getScheme()) {
                case file:
                    new DiskReachability().test(bookmark);
                    break;
                case https:
                case http:
                    new ChainedReachability(new HostnameReachability(), monitor, new ResolverReachability(proxy), new HttpReachability(proxy)).test(bookmark);
                    break;
                case sftp:
                    final Host jumphost = JumpHostConfiguratorFactory.get(bookmark.getProtocol()).getJumphost(bookmark.getHostname());
                    if(null != jumphost) {
                        log.warn("Run reachablity check for jump host {}", jumphost);
                        new ChainedReachability(new HostnameReachability(), monitor, new ResolverReachability(proxy), new TcpReachability(proxy)).test(jumphost);
                        return;
                    }
                    else {
                        new ChainedReachability(new HostnameReachability(), monitor, new ResolverReachability(proxy), new TcpReachability(proxy)).test(bookmark);
                    }
                    break;
                default:
                    new ChainedReachability(new HostnameReachability(), monitor, new ResolverReachability(proxy), new TcpReachability(proxy)).test(bookmark);
                    break;
            }
        }

        @Override
        public Monitor monitor(final Host bookmark, final Callback callback) {
            switch(bookmark.getProtocol().getScheme()) {
                case file:
                    return Monitor.disabled;
            }
            return monitor.monitor(bookmark, callback);
        }
    }
}
