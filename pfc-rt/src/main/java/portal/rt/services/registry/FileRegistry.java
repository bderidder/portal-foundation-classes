/*
 * Copyright 2000 - 2004,  Bavo De Ridder
 *
 * This file is part of Portal Foundation Classes.
 *
 * Portal Foundation Classes is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * Portal Foundation Classes is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Portal Foundation Classes; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston
 *
 * http://www.gnu.org/licenses/gpl.html
 */
package portal.rt.services.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import portal.services.registry.IRegistry;
import portal.services.registry.RegistryException;
import portal.services.registry.RegistryKey;

public class FileRegistry implements IRegistry
{
    private static final Log LOGGER = LogFactory.getLog(FileRegistry.class);

    public FileRegistry(RegistryFile registryFile) throws RegistryException
    {
        this._myRegistryFile = registryFile;

        _registryKeys = registryFile.loadRegistry();
    }

    @Override
    public Collection<RegistryKey> listRegistryKeys() throws RegistryException
    {
        Collection<RegistryKey> values = _registryKeys.values();

        return new HashSet<>(values);
    }

    @Override
    public RegistryKey getRegistryKey(String name) throws RegistryException
    {
        LOGGER.debug("Request for registry key " + name);

        if (_myRegistryFile.isModified())
        {
            _registryKeys = _myRegistryFile.loadRegistry();
        }

        return _registryKeys.get(name);
    }

    void reload() throws RegistryException
    {
        _registryKeys = _myRegistryFile.loadRegistry();
    }
    private RegistryFile _myRegistryFile;
    private Map<String, RegistryKey> _registryKeys;
}
