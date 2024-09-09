import React from "react";
import {
    useDataGrid,
    List,
    EmailField,
} from "@refinedev/mui";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { IResourceComponentsProps } from "@refinedev/core";
import { Checkbox } from "@mui/material";

export const UserList: React.FC<IResourceComponentsProps> = () => {
    const { dataGridProps } = useDataGrid();

    const columns = React.useMemo<GridColDef[]>(
        () => [
            {
                field: "id",
                headerName: "Id",
                type: "number",
                minWidth: 50,
            },
            {
                field: "username",
                flex: 1,
                headerName: "Username",
                minWidth: 250,
                renderCell: function render({ value }) {
                    return <EmailField value={value} />;
                },
            },
            {
                field: "role",
                flex: 1,
                headerName: "Role",
                minWidth: 200,
            },
            {
                field: "enabled",
                headerName: "Enabled",
                minWidth: 100,
                renderCell: function render({ value }) {
                    return <Checkbox checked={!!value} />;
                },
            },

        ],
        [],
    );

    return (
        <List>
            <DataGrid {...dataGridProps} columns={columns} autoHeight />
        </List>
    );
};
