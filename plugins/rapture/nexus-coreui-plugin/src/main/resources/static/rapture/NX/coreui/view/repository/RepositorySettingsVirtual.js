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
 * Virtual repository settings form.
 *
 * @since 3.0
 */
Ext.define('NX.coreui.view.repository.RepositorySettingsVirtual', {
  extend: 'NX.coreui.view.repository.RepositorySettings',
  alias: 'widget.nx-repository-settings-virtual',

  api: {
    submit: 'NX.direct.coreui_Repository.updateVirtual'
  },
  settingsFormSuccessMessage: function (data) {
    return 'Repository updated: ' + data['id'];
  },

  initComponent: function () {
    var me = this;

    me.repositoryStore = Ext.create('NX.coreui.store.RepositoryReference', { remoteFilter: true });
    me.repositoryStore.filter({ property: 'format', value: me.template.masterFormat });

    me.items = [
      {
        xtype: 'checkbox',
        name: 'exposed',
        fieldLabel: 'Publish URL',
        value: true
      },
      {
        xtype: 'combo',
        name: 'shadowOf',
        itemId: 'shadowOf',
        fieldLabel: 'Source repository',
        emptyText: 'select a repository',
        editable: false,
        readOnly: true,
        store: me.repositoryStore,
        queryMode: 'local',
        displayField: 'name',
        valueField: 'id'
      },
      {
        xtype: 'checkbox',
        name: 'synchronizeAtStartup',
        fieldLabel: 'Synchronize on Startup',
        value: true
      }
    ];

    me.callParent(arguments);
  }

});
