/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2014 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
/**
 * Dashboard uber mode panel.
 *
 * @since 3.0
 */
Ext.define('NX.view.dashboard.Feature', {
  extend: 'Ext.Panel',
  alias: 'widget.nx-dashboard-feature',

  // HACK: Adding something a bit more substantial for sanity, will revisit later

  layout: {
    type: 'vbox',
    align: 'stretch',
    pack: 'start'
  },

  items: [
    {
      xtype: 'label',
      text: 'Welcome',
      style: {
        'color': '#000000',
        'font-size': '20px',
        'font-weight': 'bold',
        'text-align': 'center',
        'padding': '20px'
      }
    },
    {
      xtype: 'image',
      cls: 'nx-icon-nexus-x100'
    },
    {
      xtype: 'label',
      text: 'Something more useful to be placed here in the future',
      style: {
        'text-align': 'center',
        'padding': '20px'
      }
    }
  ]
});