"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useReducer,
  useRef,
  type ReactNode,
} from "react";
import { usePathname } from "next/navigation";

import * as authApi from "@/features/auth/api";
import * as businessApi from "@/features/business/api";
import { ApiClientError } from "@/lib/api/errors";
import { resetApiSession } from "@/lib/api/client";
import { registerUnauthorizedHandler } from "@/lib/auth/auth-events";
import type { AuthPermissions, AuthUser, LoginRequest } from "@/types/auth";
import type { Business, BusinessContext } from "@/types/business";

type SessionStatus = "booting" | "authenticated" | "unauthenticated";
export type BootstrapStep = "credential" | "profile" | "business" | "context" | "ready";

interface AuthState {
  status: SessionStatus;
  user: AuthUser | null;
  access: AuthPermissions | null;
  businesses: Business[];
  businessContext: BusinessContext | null;
  activeBranchId: number | null;
  bootstrapStep: BootstrapStep;
}

interface AuthContextValue extends AuthState {
  signIn: (request: LoginRequest) => Promise<void>;
  signOut: () => Promise<void>;
  refreshProfile: () => Promise<void>;
  selectBusiness: (businessId: number) => Promise<BusinessContext>;
  refreshBusinessContext: (businessId: number) => Promise<BusinessContext>;
  selectBranch: (branchId: number) => void;
  clearBusinessContext: () => void;
  endLocalSession: () => void;
}

type Action =
  | { type: "BOOTING" }
  | { type: "BOOTSTRAP_STEP"; step: BootstrapStep }
  | { type: "UNAUTHENTICATED" }
  | { type: "HYDRATED"; user: AuthUser; access: AuthPermissions; businesses: Business[] }
  | { type: "CONTEXT_LOADING" }
  | { type: "CONTEXT_READY"; context: BusinessContext; activeBranchId: number | null }
  | { type: "BRANCH_SELECTED"; branchId: number }
  | { type: "PROFILE_UPDATED"; user: AuthUser };

const initialState: AuthState = {
  status: "booting",
  user: null,
  access: null,
  businesses: [],
  businessContext: null,
  activeBranchId: null,
  bootstrapStep: "credential",
};

function reducer(state: AuthState, action: Action): AuthState {
  switch (action.type) {
    case "BOOTING":
      return { ...initialState, status: "booting" };
    case "BOOTSTRAP_STEP":
      return { ...state, bootstrapStep: action.step };
    case "UNAUTHENTICATED":
      return { ...initialState, status: "unauthenticated" };
    case "HYDRATED":
      return {
        ...state,
        status: "authenticated",
        user: action.user,
        access: action.access,
        businesses: action.businesses,
        bootstrapStep:
          action.businesses.length > 0 && !action.access.roles.includes("PLATFORM_ADMIN")
            ? "business"
            : "ready",
      };
    case "CONTEXT_LOADING":
      return { ...state, businessContext: null, activeBranchId: null, bootstrapStep: "context" };
    case "CONTEXT_READY":
      return {
        ...state,
        businessContext: action.context,
        activeBranchId: action.activeBranchId,
        bootstrapStep: "ready",
      };
    case "BRANCH_SELECTED":
      return { ...state, activeBranchId: action.branchId };
    case "PROFILE_UPDATED":
      return { ...state, user: action.user };
  }
}

const AuthContext = createContext<AuthContextValue | null>(null);

function branchPreferenceKey(userId: number, businessId: number) {
  return `tecmarket:branch:${userId}:${businessId}`;
}

function resolveInitialBranch(context: BusinessContext) {
  if (typeof window !== "undefined") {
    const stored = window.localStorage.getItem(branchPreferenceKey(context.userId, context.businessId));
    const parsed = stored ? Number.parseInt(stored, 10) : Number.NaN;

    if (context.branches.some((branch) => branch.id === parsed)) return parsed;
  }

  return context.branches.find((branch) => branch.primaryBranch)?.id ?? context.branches[0]?.id ?? null;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, dispatch] = useReducer(reducer, initialState);
  const pathname = usePathname();
  const contextRequestId = useRef(0);

  const endLocalSession = useCallback(() => {
    contextRequestId.current += 1;
    resetApiSession();
    dispatch({ type: "UNAUTHENTICATED" });
  }, []);

  const hydrate = useCallback(async () => {
    dispatch({ type: "BOOTSTRAP_STEP", step: "profile" });
    const [user, access] = await Promise.all([authApi.getMe(), authApi.getMyPermissions()]);
    dispatch({ type: "BOOTSTRAP_STEP", step: "business" });
    const businesses = await businessApi.getMyBusinesses();

    dispatch({ type: "HYDRATED", user, access, businesses });
  }, []);

  useEffect(() => registerUnauthorizedHandler(endLocalSession), [endLocalSession]);

  useEffect(() => {
    const protectedRoute =
      pathname === "/app" ||
      pathname.startsWith("/app/") ||
      pathname === "/plataforma" ||
      pathname.startsWith("/plataforma/");
    if (!protectedRoute || state.status !== "booting") return;

    let active = true;

    async function restore() {
      try {
        await authApi.restoreAccessToken();
        if (active) await hydrate();
      } catch {
        if (active) endLocalSession();
      }
    }

    void restore();

    return () => {
      active = false;
    };
  }, [endLocalSession, hydrate, pathname, state.status]);

  const signIn = useCallback(
    async (request: LoginRequest) => {
      dispatch({ type: "BOOTING" });

      try {
        await authApi.login(request);
        await hydrate();
      } catch (error) {
        endLocalSession();
        throw error;
      }
    },
    [endLocalSession, hydrate],
  );

  const signOut = useCallback(async () => {
    try {
      await authApi.logout();
    } finally {
      endLocalSession();
    }
  }, [endLocalSession]);

  const refreshProfile = useCallback(async () => {
    const user = await authApi.getMe();
    dispatch({ type: "PROFILE_UPDATED", user });
  }, []);

  const clearBusinessContext = useCallback(() => {
    contextRequestId.current += 1;
    dispatch({ type: "CONTEXT_LOADING" });
  }, []);

  const selectBusiness = useCallback(async (businessId: number) => {
    const requestId = contextRequestId.current + 1;
    contextRequestId.current = requestId;
    dispatch({ type: "CONTEXT_LOADING" });
    const context = await businessApi.getBusinessContext(businessId);

    if (requestId !== contextRequestId.current) {
      throw new ApiClientError({ status: 409, message: "La selección de negocio fue reemplazada por una más reciente." });
    }

    if (context.businessId !== businessId) {
      throw new ApiClientError({
        status: 409,
        message: "La información recibida no corresponde al negocio solicitado.",
      });
    }

    const activeBranchId = resolveInitialBranch(context);
    dispatch({ type: "CONTEXT_READY", context, activeBranchId });

    return context;
  }, []);

  const refreshBusinessContext = useCallback(async (businessId: number) => {
    const requestId = contextRequestId.current + 1;
    contextRequestId.current = requestId;
    const context = await businessApi.getBusinessContext(businessId);

    if (requestId !== contextRequestId.current) {
      throw new ApiClientError({ status: 409, message: "La actualización fue reemplazada por una selección más reciente." });
    }

    if (context.businessId !== businessId) {
      throw new ApiClientError({
        status: 409,
        message: "La información recibida no corresponde al negocio solicitado.",
      });
    }

    const currentBranchId = state.businessContext?.businessId === businessId ? state.activeBranchId : null;
    const activeBranchId = currentBranchId && context.branches.some((branch) => branch.id === currentBranchId)
      ? currentBranchId
      : resolveInitialBranch(context);
    dispatch({ type: "CONTEXT_READY", context, activeBranchId });

    return context;
  }, [state.activeBranchId, state.businessContext?.businessId]);

  const selectBranch = useCallback(
    (branchId: number) => {
      const context = state.businessContext;

      if (!context?.branches.some((branch) => branch.id === branchId)) return;

      window.localStorage.setItem(branchPreferenceKey(context.userId, context.businessId), String(branchId));
      dispatch({ type: "BRANCH_SELECTED", branchId });
    },
    [state.businessContext],
  );

  const value = useMemo<AuthContextValue>(
    () => ({
      ...state,
      signIn,
      signOut,
      refreshProfile,
      selectBusiness,
      refreshBusinessContext,
      selectBranch,
      clearBusinessContext,
      endLocalSession,
    }),
    [
      state,
      signIn,
      signOut,
      refreshProfile,
      selectBusiness,
      refreshBusinessContext,
      selectBranch,
      clearBusinessContext,
      endLocalSession,
    ],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) throw new Error("useAuth debe usarse dentro de AuthProvider.");

  return context;
}
