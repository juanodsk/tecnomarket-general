export interface BusinessQualityTest {
  id: number;
  businessId: number;
  deviceTypeId: number;
  deviceTypeCode: string;
  deviceTypeName: string;
  code: string;
  name: string;
  description: string;
  required: boolean;
  sortOrder: number;
  active: boolean;
}

export interface CreateBusinessQualityTest {
  deviceTypeId: number;
  code: string;
  name: string;
  description: string;
  required: boolean;
  sortOrder: number;
}

export interface UpdateBusinessQualityTest extends CreateBusinessQualityTest {
  active: boolean;
}
