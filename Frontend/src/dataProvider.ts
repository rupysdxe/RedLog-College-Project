import { CrudFilters, CrudOperators, DataProvider } from "@refinedev/core";
import restDataProvider, { generateSort } from "@refinedev/simple-rest";
import { AxiosInstance } from "axios";
import { stringify } from "query-string";

import { API_URL } from "./constants";


type MethodTypes = "get" | "delete" | "head" | "options";
type MethodTypesWithBody = "post" | "put" | "patch";


const mapOperator = (operator: CrudOperators): string => {
  switch (operator) {
    case "eq":
      return "";
    default:
      throw new Error(`Operator ${operator} is not supported`);
  }
};

const generateFilter = (filters?: CrudFilters) => {
  const queryFilters: { [key: string]: string } = {};
  if (filters) {
    filters.map((filter: any) => {
      if (
        filter.operator !== "or" &&
        filter.operator !== "and" &&
        "field" in filter
      ) {
        const { field, operator, value } = filter;

        const mappedOperator = mapOperator(operator);
        queryFilters[`${field}${mappedOperator}`] = value;
      }
    });
  }

  return queryFilters;
};

export const dataProvider = (axios: AxiosInstance): DataProvider => {
  return {
    ...restDataProvider(API_URL, axios),
    getList: async ({ resource, pagination, filters, sorters, meta }) => {
      const url = `${API_URL}/${resource}`;

      const {
          current = 1,
          pageSize = 10,
          mode = "server",
      } = pagination ?? {};

      const { headers: headersFromMeta, method } = meta ?? {};
      const requestMethod = (method as MethodTypes) ?? "get";

      const queryFilters = generateFilter(filters);

      const query: {
          _start?: number;
          _end?: number;
          _sort?: string;
          _order?: string;
      } = {};

      if (mode === "server") {
          query._start = (current - 1) * pageSize;
          query._end = current * pageSize;
      }

      const generatedSort = generateSort(sorters);
      if (generatedSort) {
          const { _sort, _order } = generatedSort;
          query._sort = _sort.join(",");
          query._order = _order.join(",");
      }

      const { data, headers } = await axios[requestMethod](
          `${url}?${stringify(query)}&${stringify(queryFilters)}`,
          {
              headers: headersFromMeta,
          },
      );
      const total = +headers["x-total-count"];
      return {
          data,
          total: total || data.length,
      };
  },
    getOne: async ({ resource, id, metaData }) => {
      const url = metaData?.getComments
        ? `${API_URL}/${resource}/${id}`
        : `${API_URL}/${resource}/${id}`;

      const { data } = await axios.get(url);

      return {
        data: data[metaData?.resource || resource],
      };
    },
    update: async ({ resource, id, variables, metaData }) => {
      const url = metaData?.URLSuffix
        ? `${API_URL}/${resource}/${id}/${metaData.URLSuffix}`
        : `${API_URL}/${resource}/${id}`;

      const { data } = metaData?.URLSuffix
        ? await axios.post(url)
        : await axios.put(url, variables);

      return {
        data,
      };
    },

    deleteOne: async ({ resource, id, variables, metaData }) => {
      const url = metaData?.URLSuffix
        ? `${API_URL}/${resource}/${id}/${metaData.URLSuffix}`
        : `${API_URL}/${resource}/${id}`;

      const { data } = await axios.delete(url, {
        data: variables,
      });

      return {
        data,
      };
    },
  };
};
