--
-- PostgreSQL database dump
--

\restrict cfrvFsfsjWN4d4itvFYVdX6qnrNHt8wZ71c95uiXaW6vyAWfG9y3kiuNpkUd4jA

-- Dumped from database version 18.2
-- Dumped by pg_dump version 18.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: app_module; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.app_module (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: app_module_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.app_module_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: app_module_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.app_module_id_seq OWNED BY public.app_module.id;


--
-- Name: app_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.app_user (
    id integer NOT NULL,
    document_type_id integer NOT NULL,
    document_number character varying(255) NOT NULL,
    name character varying(50) NOT NULL,
    surname character varying(50) NOT NULL,
    email character varying(150) NOT NULL,
    password character varying(255) NOT NULL,
    address character varying(200) NOT NULL,
    phone character varying(30) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    email_verified boolean DEFAULT false,
    email_verified_at timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    country_id integer,
    department_id integer,
    city_id integer
);


--
-- Name: app_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.app_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: app_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.app_user_id_seq OWNED BY public.app_user.id;


--
-- Name: appointment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.appointment (
    id integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer NOT NULL,
    customer_device_id integer NOT NULL,
    business_service_id integer NOT NULL,
    status_id integer CONSTRAINT appointment_status_id_not_null1 NOT NULL,
    scheduled_at timestamp without time zone NOT NULL,
    problem_description character varying(200) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL
);


--
-- Name: appointment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.appointment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: appointment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.appointment_id_seq OWNED BY public.appointment.id;


--
-- Name: business; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business (
    id integer NOT NULL,
    document_type_id integer NOT NULL,
    document_number character varying(50) NOT NULL,
    name character varying(150) NOT NULL,
    legal_name character varying(200) NOT NULL,
    email character varying(150) NOT NULL,
    phone character varying(30) NOT NULL,
    address character varying(200) NOT NULL,
    description character varying(255) NOT NULL,
    city_id integer NOT NULL,
    logo character varying(500),
    cover_image character varying(500),
    rating_average numeric(3,2),
    rating_count integer,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_branch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_branch (
    id integer NOT NULL,
    business_id integer NOT NULL,
    name character varying(120) NOT NULL,
    city_id integer NOT NULL,
    address character varying(200) NOT NULL,
    neighborhood character varying(120),
    phone character varying(30),
    latitude numeric(10,7),
    longitude numeric(10,7),
    service_radius_km numeric(6,2),
    primary_branch boolean DEFAULT false NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_number integer NOT NULL
);


--
-- Name: business_branch_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_branch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_branch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_branch_id_seq OWNED BY public.business_branch.id;


--
-- Name: business_catalog_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_catalog_item (
    id integer NOT NULL,
    business_id integer NOT NULL,
    catalog_type character varying(40) NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(150) NOT NULL,
    description character varying(500),
    icon character varying(255),
    sort_order integer,
    protected_item boolean DEFAULT false NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_catalog_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_catalog_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_catalog_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_catalog_item_id_seq OWNED BY public.business_catalog_item.id;


--
-- Name: business_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_id_seq OWNED BY public.business.id;


--
-- Name: business_quality_test; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_quality_test (
    id integer NOT NULL,
    business_id integer NOT NULL,
    device_type_id integer NOT NULL,
    code character varying(30) NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(200) NOT NULL,
    required boolean DEFAULT false NOT NULL,
    sort_order integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_quality_test_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_quality_test_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_quality_test_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_quality_test_id_seq OWNED BY public.business_quality_test.id;


--
-- Name: business_request; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_request (
    id integer NOT NULL,
    requested_by_user_id integer NOT NULL,
    document_type_id integer NOT NULL,
    document_number character varying(50) NOT NULL,
    business_name character varying(150) NOT NULL,
    legal_name character varying(200),
    email character varying(150) NOT NULL,
    phone character varying(50) NOT NULL,
    city_id integer NOT NULL,
    address character varying(200),
    legal_file character varying(500) NOT NULL,
    status character varying(50) NOT NULL,
    reviewed_by_user_id integer,
    reviewed_at timestamp without time zone,
    rejection_reason text,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_request_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_request_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_request_id_seq OWNED BY public.business_request.id;


--
-- Name: business_review; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_review (
    id integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer NOT NULL,
    repair_order_id integer NOT NULL,
    rating integer NOT NULL,
    comment character varying(500),
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT ck_business_review_rating CHECK (((rating >= 1) AND (rating <= 5)))
);


--
-- Name: business_review_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_review_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_review_id_seq OWNED BY public.business_review.id;


--
-- Name: business_schedule; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_schedule (
    id integer NOT NULL,
    business_id integer NOT NULL,
    day_of_week integer NOT NULL,
    open_time time without time zone NOT NULL,
    close_time time without time zone NOT NULL,
    slot_order integer,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL,
    CONSTRAINT ck_business_schedule_day_of_week CHECK (((day_of_week >= 1) AND (day_of_week <= 7))),
    CONSTRAINT ck_business_schedule_time_range CHECK ((open_time < close_time))
);


--
-- Name: business_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_schedule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_schedule_id_seq OWNED BY public.business_schedule.id;


--
-- Name: business_service; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_service (
    id integer NOT NULL,
    business_id integer NOT NULL,
    service_category_id integer NOT NULL,
    device_type_id integer NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(200) NOT NULL,
    base_price numeric(12,2) NOT NULL,
    estimated_time_minutes integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    image_url character varying(1000),
    image_object_key character varying(700)
);


--
-- Name: business_service_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_service_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_service_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_service_id_seq OWNED BY public.business_service.id;


--
-- Name: business_subscription; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_subscription (
    id integer NOT NULL,
    business_id integer NOT NULL,
    plan_id integer NOT NULL,
    status character varying(30) NOT NULL,
    billing_cycle character varying(20) NOT NULL,
    current_period_start timestamp without time zone NOT NULL,
    current_period_end timestamp without time zone NOT NULL,
    trial_ends_at timestamp without time zone,
    cancel_at_period_end boolean DEFAULT false NOT NULL,
    payment_provider character varying(30) DEFAULT 'MANUAL'::character varying NOT NULL,
    provider_customer_id character varying(150),
    provider_subscription_id character varying(150),
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_subscription_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_subscription_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_subscription_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_subscription_id_seq OWNED BY public.business_subscription.id;


--
-- Name: business_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_user (
    id integer NOT NULL,
    business_id integer NOT NULL,
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    commission_percentage numeric(5,2) DEFAULT 0.00 NOT NULL,
    CONSTRAINT ck_business_user_commission_percentage CHECK (((commission_percentage >= 0.00) AND (commission_percentage <= 100.00)))
);


--
-- Name: business_user_branch; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.business_user_branch (
    id integer NOT NULL,
    business_user_id integer NOT NULL,
    branch_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: business_user_branch_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_user_branch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_user_branch_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_user_branch_id_seq OWNED BY public.business_user_branch.id;


--
-- Name: business_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.business_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: business_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.business_user_id_seq OWNED BY public.business_user.id;


--
-- Name: city; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.city (
    id integer NOT NULL,
    country_id integer NOT NULL,
    name character varying(120) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    department_id integer NOT NULL
);


--
-- Name: city_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.city_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: city_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.city_id_seq OWNED BY public.city.id;


--
-- Name: commission_settlement; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.commission_settlement (
    id integer NOT NULL,
    settlement_code character varying(30),
    business_id integer NOT NULL,
    business_user_id integer NOT NULL,
    period_start date NOT NULL,
    period_end date NOT NULL,
    orders_count integer NOT NULL,
    total_amount numeric(12,2) NOT NULL,
    status character varying(20) DEFAULT 'DRAFT'::character varying NOT NULL,
    notes character varying(500),
    generated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    approved_at timestamp without time zone,
    paid_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT ck_commission_settlement_period CHECK ((period_end >= period_start)),
    CONSTRAINT ck_commission_settlement_status CHECK (((status)::text = ANY ((ARRAY['DRAFT'::character varying, 'APPROVED'::character varying, 'PAID'::character varying])::text[]))),
    CONSTRAINT ck_commission_settlement_values CHECK (((orders_count > 0) AND (total_amount >= 0.00)))
);


--
-- Name: commission_settlement_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.commission_settlement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: commission_settlement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.commission_settlement_id_seq OWNED BY public.commission_settlement.id;


--
-- Name: country; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.country (
    id integer NOT NULL,
    code character varying(10) NOT NULL,
    name character varying(100) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: country_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.country_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.country_id_seq OWNED BY public.country.id;


--
-- Name: customer_device; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customer_device (
    id integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer NOT NULL,
    device_type_id integer NOT NULL,
    brand_id integer NOT NULL,
    model character varying(50) NOT NULL,
    serial_number character varying(60),
    imei character varying(60),
    registered_by_business_user_id integer NOT NULL,
    color character varying(20) NOT NULL,
    registered_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: customer_device_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_device_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_device_id_seq OWNED BY public.customer_device.id;


--
-- Name: department; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.department (
    id integer NOT NULL,
    country_id integer NOT NULL,
    code character varying(20) NOT NULL,
    name character varying(120) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    deleted_at timestamp without time zone
);


--
-- Name: department_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.department_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: department_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.department_id_seq OWNED BY public.department.id;


--
-- Name: document_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.document_type (
    id integer NOT NULL,
    code character varying(15) NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(100) NOT NULL,
    applies_to_person boolean DEFAULT true NOT NULL,
    applies_to_company boolean DEFAULT false NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: document_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.document_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: document_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.document_type_id_seq OWNED BY public.document_type.id;


--
-- Name: email_verification_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.email_verification_token (
    id integer NOT NULL,
    user_id integer NOT NULL,
    token_hash character varying(64) CONSTRAINT email_verification_token_token_not_null NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    verified_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: email_verification_token_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.email_verification_token_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: email_verification_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.email_verification_token_id_seq OWNED BY public.email_verification_token.id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


--
-- Name: inventory_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.inventory_item (
    id integer NOT NULL,
    business_id integer NOT NULL,
    name character varying(150) NOT NULL,
    item_category_id integer NOT NULL,
    description character varying(500),
    sku character varying(80) NOT NULL,
    quantity integer NOT NULL,
    minimum_stock integer NOT NULL,
    unit_cost numeric(12,2) NOT NULL,
    sale_price numeric(12,2) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL,
    CONSTRAINT chk_inventory_item_minimum_stock_non_negative CHECK ((minimum_stock >= 0)),
    CONSTRAINT chk_inventory_item_quantity_non_negative CHECK ((quantity >= 0)),
    CONSTRAINT chk_inventory_item_sale_price_non_negative CHECK ((sale_price >= (0)::numeric)),
    CONSTRAINT chk_inventory_item_unit_cost_non_negative CHECK ((unit_cost >= (0)::numeric))
);


--
-- Name: inventory_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.inventory_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: inventory_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.inventory_item_id_seq OWNED BY public.inventory_item.id;


--
-- Name: password_reset_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.password_reset_token (
    id integer NOT NULL,
    user_id integer NOT NULL,
    token_hash character varying(64) NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    used_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    deleted_at timestamp without time zone
);


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.password_reset_token_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: password_reset_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.password_reset_token_id_seq OWNED BY public.password_reset_token.id;


--
-- Name: permission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permission (
    id integer NOT NULL,
    module_id integer NOT NULL,
    code character varying(80) NOT NULL,
    name character varying(120) NOT NULL,
    description character varying(255) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    scope character varying(20) NOT NULL,
    CONSTRAINT chk_permission_scope CHECK (((scope)::text = ANY ((ARRAY['GLOBAL'::character varying, 'BUSINESS'::character varying])::text[])))
);


--
-- Name: permission_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.permission_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.permission_id_seq OWNED BY public.permission.id;


--
-- Name: quotation; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quotation (
    id integer NOT NULL,
    repair_order_id integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer NOT NULL,
    total_amount numeric(12,2) NOT NULL,
    quotation_status_id integer NOT NULL,
    notes character varying(500),
    expires_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT chk_quotation_expiration CHECK (((expires_at IS NULL) OR (created_at IS NULL) OR (expires_at > created_at))),
    CONSTRAINT chk_quotation_total_non_negative CHECK ((total_amount >= (0)::numeric))
);


--
-- Name: quotation_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.quotation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: quotation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.quotation_id_seq OWNED BY public.quotation.id;


--
-- Name: quotation_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.quotation_item (
    id integer NOT NULL,
    quotation_id integer NOT NULL,
    description character varying(300) NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(12,2) NOT NULL,
    total_price numeric(12,2) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT chk_quotation_item_prices_non_negative CHECK (((unit_price >= (0)::numeric) AND (total_price >= (0)::numeric))),
    CONSTRAINT chk_quotation_item_quantity_positive CHECK ((quantity > 0))
);


--
-- Name: quotation_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.quotation_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: quotation_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.quotation_item_id_seq OWNED BY public.quotation_item.id;


--
-- Name: refresh_token; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refresh_token (
    id integer NOT NULL,
    user_id integer NOT NULL,
    token_hash character varying(64) CONSTRAINT refresh_token_token_not_null NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    revoked boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    family_id uuid NOT NULL,
    revoked_at timestamp without time zone,
    last_used_at timestamp without time zone,
    replaced_by_token_hash character varying(64),
    client_description character varying(255),
    ip_address character varying(64)
);


--
-- Name: refresh_token_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.refresh_token_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: refresh_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.refresh_token_id_seq OWNED BY public.refresh_token.id;


--
-- Name: repair_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order (
    id integer NOT NULL,
    order_number character varying(20) NOT NULL,
    order_sequence integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer NOT NULL,
    customer_device_id integer NOT NULL,
    appointment_id integer,
    business_service_id integer NOT NULL,
    status_id integer CONSTRAINT repair_order_status_id_not_null1 NOT NULL,
    received_by_business_user_id integer NOT NULL,
    assigned_business_user_id integer NOT NULL,
    problem_description text NOT NULL,
    diagnosis text,
    estimated_price numeric(12,2),
    final_price numeric(12,2),
    received_at timestamp without time zone,
    estimated_delivery_at timestamp without time zone,
    delivered_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL,
    idempotency_key character varying(100),
    idempotency_fingerprint character varying(64),
    CONSTRAINT chk_repair_order_delivered_date CHECK (((delivered_at IS NULL) OR (received_at IS NULL) OR (delivered_at >= received_at))),
    CONSTRAINT chk_repair_order_delivery_dates CHECK (((estimated_delivery_at IS NULL) OR (received_at IS NULL) OR (estimated_delivery_at >= received_at))),
    CONSTRAINT chk_repair_order_estimated_price_non_negative CHECK (((estimated_price IS NULL) OR (estimated_price >= (0)::numeric))),
    CONSTRAINT chk_repair_order_final_price_non_negative CHECK (((final_price IS NULL) OR (final_price >= (0)::numeric)))
);


--
-- Name: repair_order_attachment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_attachment (
    id integer NOT NULL,
    repair_order_id integer NOT NULL,
    uploaded_by_business_user_id integer NOT NULL,
    file_url character varying(1000),
    description character varying(500),
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    bucket_name character varying(100),
    object_key character varying(700),
    original_file_name character varying(255),
    content_type character varying(100),
    file_size bigint
);


--
-- Name: repair_order_attachment_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_attachment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_attachment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_attachment_id_seq OWNED BY public.repair_order_attachment.id;


--
-- Name: repair_order_commission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_commission (
    id integer NOT NULL,
    business_id integer NOT NULL,
    repair_order_id integer NOT NULL,
    business_user_id integer NOT NULL,
    order_total numeric(12,2) NOT NULL,
    parts_total numeric(12,2) NOT NULL,
    commission_base numeric(12,2) NOT NULL,
    commission_percentage numeric(5,2) NOT NULL,
    commission_amount numeric(12,2) NOT NULL,
    status character varying(20) DEFAULT 'PENDING'::character varying NOT NULL,
    generated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    approved_at timestamp without time zone,
    paid_at timestamp without time zone,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    settlement_id integer NOT NULL,
    CONSTRAINT ck_repair_order_commission_percentage CHECK (((commission_percentage >= 0.00) AND (commission_percentage <= 100.00))),
    CONSTRAINT ck_repair_order_commission_status CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT ck_repair_order_commission_values CHECK (((order_total >= 0.00) AND (parts_total >= 0.00) AND (commission_base >= 0.00) AND (commission_amount >= 0.00) AND (order_total >= parts_total)))
);


--
-- Name: repair_order_commission_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_commission_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_commission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_commission_id_seq OWNED BY public.repair_order_commission.id;


--
-- Name: repair_order_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_history (
    id integer NOT NULL,
    repair_order_id integer NOT NULL,
    status_id integer NOT NULL,
    created_by_user_id integer NOT NULL,
    comment text,
    visibility character varying(20) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT ck_repair_order_history_visibility CHECK (((visibility)::text = ANY ((ARRAY['INTERNAL'::character varying, 'CUSTOMER'::character varying])::text[])))
);


--
-- Name: repair_order_history_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_history_id_seq OWNED BY public.repair_order_history.id;


--
-- Name: repair_order_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_id_seq OWNED BY public.repair_order.id;


--
-- Name: repair_order_part; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_part (
    id integer NOT NULL,
    repair_order_id integer NOT NULL,
    inventory_item_id integer NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(12,2) NOT NULL,
    total_price numeric(12,2) NOT NULL,
    used_by_business_user_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: repair_order_part_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_part_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_part_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_part_id_seq OWNED BY public.repair_order_part.id;


--
-- Name: repair_order_quality_check; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_quality_check (
    id integer NOT NULL,
    business_id integer NOT NULL,
    repair_order_id integer,
    intake_ticket_code character varying(60) NOT NULL,
    business_quality_test_id integer NOT NULL,
    checked_by_user_id integer NOT NULL,
    result character varying(20) NOT NULL,
    notes character varying(300),
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    checked_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT ck_repair_order_quality_check_result CHECK (((result)::text = ANY ((ARRAY['SI'::character varying, 'NO'::character varying, 'NO_APLICA'::character varying])::text[])))
);


--
-- Name: repair_order_quality_check_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.repair_order_quality_check_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: repair_order_quality_check_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.repair_order_quality_check_id_seq OWNED BY public.repair_order_quality_check.id;


--
-- Name: repair_order_sequence; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.repair_order_sequence (
    business_id integer NOT NULL,
    next_sequence integer NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    branch_id integer NOT NULL
);


--
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(255) NOT NULL,
    scope character varying(30) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- Name: role_permission; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_permission (
    id integer NOT NULL,
    role_id integer NOT NULL,
    permission_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: role_permission_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.role_permission_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_permission_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.role_permission_id_seq OWNED BY public.role_permission.id;


--
-- Name: sale; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sale (
    id integer NOT NULL,
    business_id integer NOT NULL,
    customer_id integer,
    sold_by_business_user_id integer NOT NULL,
    total_amount numeric(12,2) NOT NULL,
    payment_method_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL,
    idempotency_key character varying(100),
    idempotency_fingerprint character varying(64),
    CONSTRAINT chk_sale_total_non_negative CHECK ((total_amount >= (0)::numeric))
);


--
-- Name: sale_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sale_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sale_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sale_id_seq OWNED BY public.sale.id;


--
-- Name: sale_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sale_item (
    id integer NOT NULL,
    sale_id integer NOT NULL,
    inventory_item_id integer NOT NULL,
    quantity integer NOT NULL,
    unit_cost numeric(12,2) NOT NULL,
    unit_price numeric(12,2) NOT NULL,
    total_cost numeric(12,2) NOT NULL,
    total_price numeric(12,2) NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    CONSTRAINT chk_sale_item_prices_non_negative CHECK (((unit_cost >= (0)::numeric) AND (unit_price >= (0)::numeric) AND (total_cost >= (0)::numeric) AND (total_price >= (0)::numeric))),
    CONSTRAINT chk_sale_item_quantity_positive CHECK ((quantity > 0))
);


--
-- Name: sale_item_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.sale_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: sale_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.sale_item_id_seq OWNED BY public.sale_item.id;


--
-- Name: stock_movement; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stock_movement (
    id integer NOT NULL,
    inventory_item_id integer NOT NULL,
    repair_order_id integer,
    business_user_id integer NOT NULL,
    movement_type_id integer NOT NULL,
    sale_item_id integer,
    quantity integer NOT NULL,
    notes character varying(500),
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    branch_id integer NOT NULL,
    idempotency_key character varying(100),
    idempotency_fingerprint character varying(64),
    CONSTRAINT chk_stock_movement_quantity_positive CHECK ((quantity > 0))
);


--
-- Name: stock_movement_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.stock_movement_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: stock_movement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.stock_movement_id_seq OWNED BY public.stock_movement.id;


--
-- Name: subscription_plan; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subscription_plan (
    id integer NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(120) NOT NULL,
    monthly_price numeric(12,2) NOT NULL,
    yearly_price numeric(12,2) NOT NULL,
    included_users integer NOT NULL,
    max_businesses integer NOT NULL,
    features text NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone,
    max_branches integer DEFAULT 1 NOT NULL
);


--
-- Name: subscription_plan_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.subscription_plan_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: subscription_plan_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.subscription_plan_id_seq OWNED BY public.subscription_plan.id;


--
-- Name: subscription_usage; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.subscription_usage (
    id integer NOT NULL,
    business_subscription_id integer NOT NULL,
    users_count integer DEFAULT 0 NOT NULL,
    orders_count integer DEFAULT 0 NOT NULL,
    storage_used bigint DEFAULT 0 NOT NULL,
    updated_at timestamp without time zone,
    branches_count integer DEFAULT 0 NOT NULL
);


--
-- Name: subscription_usage_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.subscription_usage_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: subscription_usage_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.subscription_usage_id_seq OWNED BY public.subscription_usage.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_role (
    id integer NOT NULL,
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    deleted_at timestamp without time zone
);


--
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_role_id_seq OWNED BY public.user_role.id;


--
-- Name: app_module id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_module ALTER COLUMN id SET DEFAULT nextval('public.app_module_id_seq'::regclass);


--
-- Name: app_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user ALTER COLUMN id SET DEFAULT nextval('public.app_user_id_seq'::regclass);


--
-- Name: appointment id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment ALTER COLUMN id SET DEFAULT nextval('public.appointment_id_seq'::regclass);


--
-- Name: business id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business ALTER COLUMN id SET DEFAULT nextval('public.business_id_seq'::regclass);


--
-- Name: business_branch id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_branch ALTER COLUMN id SET DEFAULT nextval('public.business_branch_id_seq'::regclass);


--
-- Name: business_catalog_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_catalog_item ALTER COLUMN id SET DEFAULT nextval('public.business_catalog_item_id_seq'::regclass);


--
-- Name: business_quality_test id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_quality_test ALTER COLUMN id SET DEFAULT nextval('public.business_quality_test_id_seq'::regclass);


--
-- Name: business_request id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request ALTER COLUMN id SET DEFAULT nextval('public.business_request_id_seq'::regclass);


--
-- Name: business_review id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_review ALTER COLUMN id SET DEFAULT nextval('public.business_review_id_seq'::regclass);


--
-- Name: business_schedule id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_schedule ALTER COLUMN id SET DEFAULT nextval('public.business_schedule_id_seq'::regclass);


--
-- Name: business_service id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service ALTER COLUMN id SET DEFAULT nextval('public.business_service_id_seq'::regclass);


--
-- Name: business_subscription id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_subscription ALTER COLUMN id SET DEFAULT nextval('public.business_subscription_id_seq'::regclass);


--
-- Name: business_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user ALTER COLUMN id SET DEFAULT nextval('public.business_user_id_seq'::regclass);


--
-- Name: business_user_branch id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user_branch ALTER COLUMN id SET DEFAULT nextval('public.business_user_branch_id_seq'::regclass);


--
-- Name: city id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city ALTER COLUMN id SET DEFAULT nextval('public.city_id_seq'::regclass);


--
-- Name: commission_settlement id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.commission_settlement ALTER COLUMN id SET DEFAULT nextval('public.commission_settlement_id_seq'::regclass);


--
-- Name: country id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.country ALTER COLUMN id SET DEFAULT nextval('public.country_id_seq'::regclass);


--
-- Name: customer_device id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device ALTER COLUMN id SET DEFAULT nextval('public.customer_device_id_seq'::regclass);


--
-- Name: department id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department ALTER COLUMN id SET DEFAULT nextval('public.department_id_seq'::regclass);


--
-- Name: document_type id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.document_type ALTER COLUMN id SET DEFAULT nextval('public.document_type_id_seq'::regclass);


--
-- Name: email_verification_token id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.email_verification_token ALTER COLUMN id SET DEFAULT nextval('public.email_verification_token_id_seq'::regclass);


--
-- Name: inventory_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item ALTER COLUMN id SET DEFAULT nextval('public.inventory_item_id_seq'::regclass);


--
-- Name: password_reset_token id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token ALTER COLUMN id SET DEFAULT nextval('public.password_reset_token_id_seq'::regclass);


--
-- Name: permission id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permission ALTER COLUMN id SET DEFAULT nextval('public.permission_id_seq'::regclass);


--
-- Name: quotation id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation ALTER COLUMN id SET DEFAULT nextval('public.quotation_id_seq'::regclass);


--
-- Name: quotation_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation_item ALTER COLUMN id SET DEFAULT nextval('public.quotation_item_id_seq'::regclass);


--
-- Name: refresh_token id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token ALTER COLUMN id SET DEFAULT nextval('public.refresh_token_id_seq'::regclass);


--
-- Name: repair_order id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order ALTER COLUMN id SET DEFAULT nextval('public.repair_order_id_seq'::regclass);


--
-- Name: repair_order_attachment id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_attachment ALTER COLUMN id SET DEFAULT nextval('public.repair_order_attachment_id_seq'::regclass);


--
-- Name: repair_order_commission id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission ALTER COLUMN id SET DEFAULT nextval('public.repair_order_commission_id_seq'::regclass);


--
-- Name: repair_order_history id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_history ALTER COLUMN id SET DEFAULT nextval('public.repair_order_history_id_seq'::regclass);


--
-- Name: repair_order_part id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_part ALTER COLUMN id SET DEFAULT nextval('public.repair_order_part_id_seq'::regclass);


--
-- Name: repair_order_quality_check id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check ALTER COLUMN id SET DEFAULT nextval('public.repair_order_quality_check_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- Name: role_permission id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission ALTER COLUMN id SET DEFAULT nextval('public.role_permission_id_seq'::regclass);


--
-- Name: sale id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale ALTER COLUMN id SET DEFAULT nextval('public.sale_id_seq'::regclass);


--
-- Name: sale_item id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale_item ALTER COLUMN id SET DEFAULT nextval('public.sale_item_id_seq'::regclass);


--
-- Name: stock_movement id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement ALTER COLUMN id SET DEFAULT nextval('public.stock_movement_id_seq'::regclass);


--
-- Name: subscription_plan id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subscription_plan ALTER COLUMN id SET DEFAULT nextval('public.subscription_plan_id_seq'::regclass);


--
-- Name: subscription_usage id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subscription_usage ALTER COLUMN id SET DEFAULT nextval('public.subscription_usage_id_seq'::regclass);


--
-- Name: user_role id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role ALTER COLUMN id SET DEFAULT nextval('public.user_role_id_seq'::regclass);


--
-- Name: app_module app_module_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_module
    ADD CONSTRAINT app_module_pkey PRIMARY KEY (id);


--
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);


--
-- Name: appointment appointment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT appointment_pkey PRIMARY KEY (id);


--
-- Name: business_branch business_branch_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_branch
    ADD CONSTRAINT business_branch_pkey PRIMARY KEY (id);


--
-- Name: business_catalog_item business_catalog_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_catalog_item
    ADD CONSTRAINT business_catalog_item_pkey PRIMARY KEY (id);


--
-- Name: business business_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business
    ADD CONSTRAINT business_pkey PRIMARY KEY (id);


--
-- Name: business_quality_test business_quality_test_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_quality_test
    ADD CONSTRAINT business_quality_test_pkey PRIMARY KEY (id);


--
-- Name: business_request business_request_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request
    ADD CONSTRAINT business_request_pkey PRIMARY KEY (id);


--
-- Name: business_review business_review_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_review
    ADD CONSTRAINT business_review_pkey PRIMARY KEY (id);


--
-- Name: business_schedule business_schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_schedule
    ADD CONSTRAINT business_schedule_pkey PRIMARY KEY (id);


--
-- Name: business_service business_service_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service
    ADD CONSTRAINT business_service_pkey PRIMARY KEY (id);


--
-- Name: business_subscription business_subscription_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_subscription
    ADD CONSTRAINT business_subscription_pkey PRIMARY KEY (id);


--
-- Name: business_user_branch business_user_branch_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user_branch
    ADD CONSTRAINT business_user_branch_pkey PRIMARY KEY (id);


--
-- Name: business_user business_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT business_user_pkey PRIMARY KEY (id);


--
-- Name: city city_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);


--
-- Name: commission_settlement commission_settlement_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.commission_settlement
    ADD CONSTRAINT commission_settlement_pkey PRIMARY KEY (id);


--
-- Name: commission_settlement commission_settlement_settlement_code_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.commission_settlement
    ADD CONSTRAINT commission_settlement_settlement_code_key UNIQUE (settlement_code);


--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: customer_device customer_device_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT customer_device_pkey PRIMARY KEY (id);


--
-- Name: department department_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT department_pkey PRIMARY KEY (id);


--
-- Name: document_type document_type_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.document_type
    ADD CONSTRAINT document_type_pkey PRIMARY KEY (id);


--
-- Name: email_verification_token email_verification_token_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.email_verification_token
    ADD CONSTRAINT email_verification_token_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: inventory_item inventory_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT inventory_item_pkey PRIMARY KEY (id);


--
-- Name: password_reset_token password_reset_token_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT password_reset_token_pkey PRIMARY KEY (id);


--
-- Name: permission permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);


--
-- Name: quotation_item quotation_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation_item
    ADD CONSTRAINT quotation_item_pkey PRIMARY KEY (id);


--
-- Name: quotation quotation_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation
    ADD CONSTRAINT quotation_pkey PRIMARY KEY (id);


--
-- Name: refresh_token refresh_token_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (id);


--
-- Name: refresh_token refresh_token_token_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_token_key UNIQUE (token_hash);


--
-- Name: repair_order_attachment repair_order_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_attachment
    ADD CONSTRAINT repair_order_attachment_pkey PRIMARY KEY (id);


--
-- Name: repair_order_commission repair_order_commission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT repair_order_commission_pkey PRIMARY KEY (id);


--
-- Name: repair_order_history repair_order_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_history
    ADD CONSTRAINT repair_order_history_pkey PRIMARY KEY (id);


--
-- Name: repair_order_part repair_order_part_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_part
    ADD CONSTRAINT repair_order_part_pkey PRIMARY KEY (id);


--
-- Name: repair_order repair_order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT repair_order_pkey PRIMARY KEY (id);


--
-- Name: repair_order_quality_check repair_order_quality_check_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check
    ADD CONSTRAINT repair_order_quality_check_pkey PRIMARY KEY (id);


--
-- Name: repair_order_sequence repair_order_sequence_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_sequence
    ADD CONSTRAINT repair_order_sequence_pkey PRIMARY KEY (business_id, branch_id);


--
-- Name: role_permission role_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT role_permission_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: sale_item sale_item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale_item
    ADD CONSTRAINT sale_item_pkey PRIMARY KEY (id);


--
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id);


--
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id);


--
-- Name: subscription_plan subscription_plan_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subscription_plan
    ADD CONSTRAINT subscription_plan_pkey PRIMARY KEY (id);


--
-- Name: subscription_usage subscription_usage_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subscription_usage
    ADD CONSTRAINT subscription_usage_pkey PRIMARY KEY (id);


--
-- Name: app_user uk_app_user_email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uk_app_user_email UNIQUE (email);


--
-- Name: appointment uq_appointment_id_business; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT uq_appointment_id_business UNIQUE (id, business_id);


--
-- Name: business_branch uq_business_branch_id_business; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_branch
    ADD CONSTRAINT uq_business_branch_id_business UNIQUE (id, business_id);


--
-- Name: business_service uq_business_service_id_business; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service
    ADD CONSTRAINT uq_business_service_id_business UNIQUE (id, business_id);


--
-- Name: business_user uq_business_user; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT uq_business_user UNIQUE (business_id, user_id, role_id);


--
-- Name: business_user uq_business_user_id_business; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT uq_business_user_id_business UNIQUE (id, business_id);


--
-- Name: customer_device uq_customer_device_id_business_customer; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT uq_customer_device_id_business_customer UNIQUE (id, business_id, customer_id);


--
-- Name: inventory_item uq_inventory_item_id_branch; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT uq_inventory_item_id_branch UNIQUE (id, branch_id);


--
-- Name: inventory_item uq_inventory_item_id_business_branch; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT uq_inventory_item_id_business_branch UNIQUE (id, business_id, branch_id);


--
-- Name: password_reset_token uq_password_reset_token_hash; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT uq_password_reset_token_hash UNIQUE (token_hash);


--
-- Name: repair_order_commission uq_repair_order_commission_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT uq_repair_order_commission_order UNIQUE (repair_order_id);


--
-- Name: repair_order uq_repair_order_id_business; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT uq_repair_order_id_business UNIQUE (id, business_id);


--
-- Name: role_permission uq_role_permission; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT uq_role_permission UNIQUE (role_id, permission_id);


--
-- Name: app_user uq_user_document; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT uq_user_document UNIQUE (document_number, document_type_id);


--
-- Name: user_role uq_user_role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT uq_user_role UNIQUE (user_id, role_id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_app_user_city_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_app_user_city_id ON public.app_user USING btree (city_id);


--
-- Name: idx_app_user_country_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_app_user_country_id ON public.app_user USING btree (country_id);


--
-- Name: idx_app_user_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_app_user_department_id ON public.app_user USING btree (department_id);


--
-- Name: idx_appointment_business_branch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_appointment_business_branch ON public.appointment USING btree (business_id, branch_id, scheduled_at DESC) WHERE (deleted_at IS NULL);


--
-- Name: idx_appointment_business_scheduled_at; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_appointment_business_scheduled_at ON public.appointment USING btree (business_id, scheduled_at);


--
-- Name: idx_business_branch_business_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_branch_business_active ON public.business_branch USING btree (business_id, active) WHERE (deleted_at IS NULL);


--
-- Name: idx_business_branch_city_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_branch_city_active ON public.business_branch USING btree (city_id, active) WHERE (deleted_at IS NULL);


--
-- Name: idx_business_catalog_item_business_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_catalog_item_business_type ON public.business_catalog_item USING btree (business_id, catalog_type, active, sort_order, name);


--
-- Name: idx_business_review_business_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_review_business_created ON public.business_review USING btree (business_id, created_at DESC, id DESC);


--
-- Name: idx_business_schedule_branch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_schedule_branch ON public.business_schedule USING btree (business_id, branch_id, day_of_week) WHERE (deleted_at IS NULL);


--
-- Name: idx_business_subscription_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_subscription_status ON public.business_subscription USING btree (status, active, current_period_end);


--
-- Name: idx_business_user_branch_access; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_business_user_branch_access ON public.business_user_branch USING btree (branch_id, business_user_id, active) WHERE (deleted_at IS NULL);


--
-- Name: idx_city_department_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_city_department_id ON public.city USING btree (department_id);


--
-- Name: idx_commission_settlement_business; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_commission_settlement_business ON public.commission_settlement USING btree (business_id);


--
-- Name: idx_commission_settlement_business_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_commission_settlement_business_user ON public.commission_settlement USING btree (business_id, business_user_id);


--
-- Name: idx_commission_settlement_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_commission_settlement_status ON public.commission_settlement USING btree (business_id, status);


--
-- Name: idx_email_verification_token_hash; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX idx_email_verification_token_hash ON public.email_verification_token USING btree (token_hash);


--
-- Name: idx_email_verification_token_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_email_verification_token_user_id ON public.email_verification_token USING btree (user_id);


--
-- Name: idx_inventory_item_business_branch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_inventory_item_business_branch ON public.inventory_item USING btree (business_id, branch_id, name) WHERE (deleted_at IS NULL);


--
-- Name: idx_inventory_item_business_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_inventory_item_business_created ON public.inventory_item USING btree (business_id, created_at DESC, id DESC);


--
-- Name: idx_password_reset_token_user_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_password_reset_token_user_active ON public.password_reset_token USING btree (user_id, active, expires_at);


--
-- Name: idx_quality_check_business_repair_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_quality_check_business_repair_order ON public.repair_order_quality_check USING btree (business_id, repair_order_id, created_at DESC, id DESC);


--
-- Name: idx_quotation_business_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_quotation_business_created ON public.quotation USING btree (business_id, created_at DESC, id DESC);


--
-- Name: idx_quotation_item_quotation; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_quotation_item_quotation ON public.quotation_item USING btree (quotation_id, created_at, id);


--
-- Name: idx_quotation_repair_order; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_quotation_repair_order ON public.quotation USING btree (repair_order_id, created_at DESC, id DESC);


--
-- Name: idx_refresh_token_family_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_token_family_id ON public.refresh_token USING btree (family_id);


--
-- Name: idx_refresh_token_user_active; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_token_user_active ON public.refresh_token USING btree (user_id, expires_at) WHERE (revoked = false);


--
-- Name: idx_refresh_token_user_active_expiration; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_token_user_active_expiration ON public.refresh_token USING btree (user_id, revoked, expires_at, created_at);


--
-- Name: idx_refresh_token_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refresh_token_user_id ON public.refresh_token USING btree (user_id);


--
-- Name: idx_repair_order_attachment_object; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_attachment_object ON public.repair_order_attachment USING btree (bucket_name, object_key) WHERE (object_key IS NOT NULL);


--
-- Name: idx_repair_order_attachment_order_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_attachment_order_created ON public.repair_order_attachment USING btree (repair_order_id, created_at DESC, id DESC);


--
-- Name: idx_repair_order_business_branch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_business_branch ON public.repair_order USING btree (business_id, branch_id, received_at DESC) WHERE (deleted_at IS NULL);


--
-- Name: idx_repair_order_commission_business; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_commission_business ON public.repair_order_commission USING btree (business_id);


--
-- Name: idx_repair_order_commission_business_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_commission_business_user ON public.repair_order_commission USING btree (business_id, business_user_id);


--
-- Name: idx_repair_order_commission_settlement; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_commission_settlement ON public.repair_order_commission USING btree (settlement_id);


--
-- Name: idx_repair_order_commission_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_commission_status ON public.repair_order_commission USING btree (business_id, status);


--
-- Name: idx_repair_order_history_order_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_history_order_created ON public.repair_order_history USING btree (repair_order_id, created_at DESC, id DESC);


--
-- Name: idx_repair_order_part_order_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_repair_order_part_order_created ON public.repair_order_part USING btree (repair_order_id, created_at DESC, id DESC);


--
-- Name: idx_sale_business_branch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sale_business_branch ON public.sale USING btree (business_id, branch_id, created_at DESC) WHERE (deleted_at IS NULL);


--
-- Name: idx_sale_business_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sale_business_created ON public.sale USING btree (business_id, created_at DESC, id DESC);


--
-- Name: idx_sale_item_sale_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_sale_item_sale_created ON public.sale_item USING btree (sale_id, created_at, id);


--
-- Name: idx_stock_movement_inventory_created; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_stock_movement_inventory_created ON public.stock_movement USING btree (inventory_item_id, created_at DESC, id DESC);


--
-- Name: uk_department_country_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_department_country_code_active ON public.department USING btree (country_id, lower((code)::text)) WHERE (deleted_at IS NULL);


--
-- Name: uk_department_country_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_department_country_name_active ON public.department USING btree (country_id, lower((name)::text)) WHERE (deleted_at IS NULL);


--
-- Name: uq_app_module_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_app_module_code_active ON public.app_module USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_branch_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_branch_name_active ON public.business_branch USING btree (business_id, lower((name)::text)) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_branch_number_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_branch_number_active ON public.business_branch USING btree (business_id, branch_number) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_branch_primary_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_branch_primary_active ON public.business_branch USING btree (business_id) WHERE ((primary_branch = true) AND (deleted_at IS NULL));


--
-- Name: uq_business_catalog_item_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_catalog_item_code_active ON public.business_catalog_item USING btree (business_id, catalog_type, code) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_quality_test_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_quality_test_code_active ON public.business_quality_test USING btree (business_id, device_type_id, code) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_review_repair_order; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_review_repair_order ON public.business_review USING btree (repair_order_id) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_schedule_branch_slot_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_schedule_branch_slot_active ON public.business_schedule USING btree (branch_id, day_of_week, slot_order) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_subscription_active_business; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_subscription_active_business ON public.business_subscription USING btree (business_id) WHERE ((active = true) AND (deleted_at IS NULL));


--
-- Name: uq_business_user_active_pair; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_user_active_pair ON public.business_user USING btree (business_id, user_id) WHERE (deleted_at IS NULL);


--
-- Name: uq_business_user_branch_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_business_user_branch_active ON public.business_user_branch USING btree (business_user_id, branch_id) WHERE (deleted_at IS NULL);


--
-- Name: uq_city_country_name_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_city_country_name_active ON public.city USING btree (country_id, lower((name)::text)) WHERE (deleted_at IS NULL);


--
-- Name: uq_country_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_country_code_active ON public.country USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_customer_device_business_imei_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_customer_device_business_imei_active ON public.customer_device USING btree (business_id, imei) WHERE ((deleted_at IS NULL) AND (imei IS NOT NULL) AND (btrim((imei)::text) <> ''::text));


--
-- Name: uq_document_type_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_document_type_code_active ON public.document_type USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_inventory_item_branch_sku; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_inventory_item_branch_sku ON public.inventory_item USING btree (branch_id, sku) WHERE (deleted_at IS NULL);


--
-- Name: uq_permission_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_permission_code_active ON public.permission USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_quality_check_repair_order_test_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_quality_check_repair_order_test_active ON public.repair_order_quality_check USING btree (business_id, repair_order_id, business_quality_test_id) WHERE ((deleted_at IS NULL) AND (repair_order_id IS NOT NULL));


--
-- Name: uq_quality_check_ticket_test_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_quality_check_ticket_test_active ON public.repair_order_quality_check USING btree (business_id, intake_ticket_code, business_quality_test_id) WHERE (deleted_at IS NULL);


--
-- Name: uq_refresh_token_hash; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_refresh_token_hash ON public.refresh_token USING btree (token_hash);


--
-- Name: uq_repair_order_branch_sequence; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_repair_order_branch_sequence ON public.repair_order USING btree (branch_id, order_sequence) WHERE (deleted_at IS NULL);


--
-- Name: uq_repair_order_business_idempotency; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_repair_order_business_idempotency ON public.repair_order USING btree (business_id, idempotency_key) WHERE (idempotency_key IS NOT NULL);


--
-- Name: uq_repair_order_business_number; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_repair_order_business_number ON public.repair_order USING btree (business_id, order_number) WHERE (deleted_at IS NULL);


--
-- Name: uq_role_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_role_code_active ON public.role USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_sale_business_idempotency; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_sale_business_idempotency ON public.sale USING btree (business_id, idempotency_key) WHERE (idempotency_key IS NOT NULL);


--
-- Name: uq_stock_movement_branch_idempotency; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_stock_movement_branch_idempotency ON public.stock_movement USING btree (branch_id, idempotency_key) WHERE (idempotency_key IS NOT NULL);


--
-- Name: uq_subscription_plan_code_active; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_subscription_plan_code_active ON public.subscription_plan USING btree (code) WHERE (deleted_at IS NULL);


--
-- Name: uq_subscription_usage_subscription; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uq_subscription_usage_subscription ON public.subscription_usage USING btree (business_subscription_id);


--
-- Name: app_user fk_app_user_city; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fk_app_user_city FOREIGN KEY (city_id) REFERENCES public.city(id);


--
-- Name: app_user fk_app_user_country; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fk_app_user_country FOREIGN KEY (country_id) REFERENCES public.country(id);


--
-- Name: app_user fk_app_user_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fk_app_user_department FOREIGN KEY (department_id) REFERENCES public.department(id);


--
-- Name: appointment fk_app_user_id_appointment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_app_user_id_appointment FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: business_review fk_app_user_id_business_review; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_review
    ADD CONSTRAINT fk_app_user_id_business_review FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: business_user fk_app_user_id_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT fk_app_user_id_business_user FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- Name: repair_order fk_app_user_id_repair_order; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_app_user_id_repair_order FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: repair_order_history fk_app_user_id_repair_order_history; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_history
    ADD CONSTRAINT fk_app_user_id_repair_order_history FOREIGN KEY (created_by_user_id) REFERENCES public.app_user(id);


--
-- Name: appointment fk_appointment_branch_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_appointment_branch_business FOREIGN KEY (branch_id, business_id) REFERENCES public.business_branch(id, business_id);


--
-- Name: appointment fk_appointment_device_business_customer; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_appointment_device_business_customer FOREIGN KEY (customer_device_id, business_id, customer_id) REFERENCES public.customer_device(id, business_id, customer_id);


--
-- Name: appointment fk_appointment_service_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_appointment_service_business FOREIGN KEY (business_service_id, business_id) REFERENCES public.business_service(id, business_id);


--
-- Name: appointment fk_appointment_status_id_appointment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_appointment_status_id_appointment FOREIGN KEY (status_id) REFERENCES public.business_catalog_item(id);


--
-- Name: stock_movement fk_branch_id_stock_movement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_branch_id_stock_movement FOREIGN KEY (branch_id) REFERENCES public.business_branch(id);


--
-- Name: customer_device fk_brand_id_customer_device; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT fk_brand_id_customer_device FOREIGN KEY (brand_id) REFERENCES public.business_catalog_item(id);


--
-- Name: appointment fk_business_id_appointment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.appointment
    ADD CONSTRAINT fk_business_id_appointment FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_branch fk_business_id_business_branch; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_branch
    ADD CONSTRAINT fk_business_id_business_branch FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_catalog_item fk_business_id_business_catalog_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_catalog_item
    ADD CONSTRAINT fk_business_id_business_catalog_item FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_quality_test fk_business_id_business_quality_test; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_quality_test
    ADD CONSTRAINT fk_business_id_business_quality_test FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_review fk_business_id_business_review; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_review
    ADD CONSTRAINT fk_business_id_business_review FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_schedule fk_business_id_business_schedule; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_schedule
    ADD CONSTRAINT fk_business_id_business_schedule FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_service fk_business_id_business_service; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service
    ADD CONSTRAINT fk_business_id_business_service FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_subscription fk_business_id_business_subscription; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_subscription
    ADD CONSTRAINT fk_business_id_business_subscription FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: business_user fk_business_id_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT fk_business_id_business_user FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: customer_device fk_business_id_customer_device; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT fk_business_id_customer_device FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: inventory_item fk_business_id_inventory_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT fk_business_id_inventory_item FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: quotation fk_business_id_quotation; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation
    ADD CONSTRAINT fk_business_id_quotation FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: repair_order fk_business_id_repair_order; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_business_id_repair_order FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: repair_order_quality_check fk_business_id_repair_order_quality_check; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check
    ADD CONSTRAINT fk_business_id_repair_order_quality_check FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: sale fk_business_id_sale; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT fk_business_id_sale FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: repair_order_quality_check fk_business_quality_test_id_repair_order_quality_check; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check
    ADD CONSTRAINT fk_business_quality_test_id_repair_order_quality_check FOREIGN KEY (business_quality_test_id) REFERENCES public.business_quality_test(id);


--
-- Name: business_request fk_business_request_requested_by_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request
    ADD CONSTRAINT fk_business_request_requested_by_user FOREIGN KEY (requested_by_user_id) REFERENCES public.app_user(id);


--
-- Name: business_request fk_business_request_reviewed_by_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request
    ADD CONSTRAINT fk_business_request_reviewed_by_user FOREIGN KEY (reviewed_by_user_id) REFERENCES public.app_user(id);


--
-- Name: subscription_usage fk_business_subscription_id_subscription_usage; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.subscription_usage
    ADD CONSTRAINT fk_business_subscription_id_subscription_usage FOREIGN KEY (business_subscription_id) REFERENCES public.business_subscription(id);


--
-- Name: business_user_branch fk_business_user_branch_branch; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user_branch
    ADD CONSTRAINT fk_business_user_branch_branch FOREIGN KEY (branch_id) REFERENCES public.business_branch(id);


--
-- Name: business_user_branch fk_business_user_branch_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user_branch
    ADD CONSTRAINT fk_business_user_branch_business_user FOREIGN KEY (business_user_id) REFERENCES public.business_user(id);


--
-- Name: repair_order_attachment fk_business_user_id_repair_order_attachment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_attachment
    ADD CONSTRAINT fk_business_user_id_repair_order_attachment FOREIGN KEY (uploaded_by_business_user_id) REFERENCES public.business_user(id);


--
-- Name: repair_order_part fk_business_user_id_repair_order_part; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_part
    ADD CONSTRAINT fk_business_user_id_repair_order_part FOREIGN KEY (used_by_business_user_id) REFERENCES public.business_user(id);


--
-- Name: stock_movement fk_business_user_id_stock_movement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_business_user_id_stock_movement FOREIGN KEY (business_user_id) REFERENCES public.business_user(id);


--
-- Name: repair_order_quality_check fk_checked_by_user_id_repair_order_quality_check; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check
    ADD CONSTRAINT fk_checked_by_user_id_repair_order_quality_check FOREIGN KEY (checked_by_user_id) REFERENCES public.app_user(id);


--
-- Name: city fk_city_country; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT fk_city_country FOREIGN KEY (country_id) REFERENCES public.country(id);


--
-- Name: city fk_city_department; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.city
    ADD CONSTRAINT fk_city_department FOREIGN KEY (department_id) REFERENCES public.department(id);


--
-- Name: business fk_city_id_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business
    ADD CONSTRAINT fk_city_id_business FOREIGN KEY (city_id) REFERENCES public.city(id);


--
-- Name: business_branch fk_city_id_business_branch; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_branch
    ADD CONSTRAINT fk_city_id_business_branch FOREIGN KEY (city_id) REFERENCES public.city(id);


--
-- Name: business_request fk_city_id_business_request; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request
    ADD CONSTRAINT fk_city_id_business_request FOREIGN KEY (city_id) REFERENCES public.city(id);


--
-- Name: commission_settlement fk_commission_settlement_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.commission_settlement
    ADD CONSTRAINT fk_commission_settlement_business FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: commission_settlement fk_commission_settlement_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.commission_settlement
    ADD CONSTRAINT fk_commission_settlement_business_user FOREIGN KEY (business_user_id) REFERENCES public.business_user(id);


--
-- Name: customer_device fk_customer_device_registrar_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT fk_customer_device_registrar_business FOREIGN KEY (registered_by_business_user_id, business_id) REFERENCES public.business_user(id, business_id);


--
-- Name: quotation fk_customer_id_quotation; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation
    ADD CONSTRAINT fk_customer_id_quotation FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: sale fk_customer_id_sale; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT fk_customer_id_sale FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: department fk_department_country; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department
    ADD CONSTRAINT fk_department_country FOREIGN KEY (country_id) REFERENCES public.country(id);


--
-- Name: business_quality_test fk_device_type_id_business_quality_test; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_quality_test
    ADD CONSTRAINT fk_device_type_id_business_quality_test FOREIGN KEY (device_type_id) REFERENCES public.business_catalog_item(id);


--
-- Name: business_service fk_device_type_id_business_service; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service
    ADD CONSTRAINT fk_device_type_id_business_service FOREIGN KEY (device_type_id) REFERENCES public.business_catalog_item(id);


--
-- Name: customer_device fk_device_type_id_customer_device; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT fk_device_type_id_customer_device FOREIGN KEY (device_type_id) REFERENCES public.business_catalog_item(id);


--
-- Name: business fk_document_type_id_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business
    ADD CONSTRAINT fk_document_type_id_business FOREIGN KEY (document_type_id) REFERENCES public.document_type(id);


--
-- Name: business_request fk_document_type_id_business_request; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_request
    ADD CONSTRAINT fk_document_type_id_business_request FOREIGN KEY (document_type_id) REFERENCES public.document_type(id);


--
-- Name: app_user fk_document_type_id_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT fk_document_type_id_user FOREIGN KEY (document_type_id) REFERENCES public.document_type(id);


--
-- Name: email_verification_token fk_email_verification_token_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.email_verification_token
    ADD CONSTRAINT fk_email_verification_token_user FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- Name: inventory_item fk_inventory_item_branch_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT fk_inventory_item_branch_business FOREIGN KEY (branch_id, business_id) REFERENCES public.business_branch(id, business_id);


--
-- Name: repair_order_part fk_inventory_item_id_repair_order_part; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_part
    ADD CONSTRAINT fk_inventory_item_id_repair_order_part FOREIGN KEY (inventory_item_id) REFERENCES public.inventory_item(id);


--
-- Name: sale_item fk_inventory_item_id_sale_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale_item
    ADD CONSTRAINT fk_inventory_item_id_sale_item FOREIGN KEY (inventory_item_id) REFERENCES public.inventory_item(id);


--
-- Name: inventory_item fk_item_category_id_inventory_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.inventory_item
    ADD CONSTRAINT fk_item_category_id_inventory_item FOREIGN KEY (item_category_id) REFERENCES public.business_catalog_item(id);


--
-- Name: stock_movement fk_movement_type_id_stock_movement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_movement_type_id_stock_movement FOREIGN KEY (movement_type_id) REFERENCES public.business_catalog_item(id);


--
-- Name: password_reset_token fk_password_reset_token_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.password_reset_token
    ADD CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- Name: sale fk_payment_method_id_sale; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT fk_payment_method_id_sale FOREIGN KEY (payment_method_id) REFERENCES public.business_catalog_item(id);


--
-- Name: permission fk_permission_module; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT fk_permission_module FOREIGN KEY (module_id) REFERENCES public.app_module(id);


--
-- Name: business_subscription fk_plan_id_business_subscription; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_subscription
    ADD CONSTRAINT fk_plan_id_business_subscription FOREIGN KEY (plan_id) REFERENCES public.subscription_plan(id);


--
-- Name: quotation_item fk_quotation_id_quotation_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation_item
    ADD CONSTRAINT fk_quotation_id_quotation_item FOREIGN KEY (quotation_id) REFERENCES public.quotation(id);


--
-- Name: quotation fk_quotation_repair_order_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation
    ADD CONSTRAINT fk_quotation_repair_order_business FOREIGN KEY (repair_order_id, business_id) REFERENCES public.repair_order(id, business_id);


--
-- Name: quotation fk_quotation_status_id_quotation; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.quotation
    ADD CONSTRAINT fk_quotation_status_id_quotation FOREIGN KEY (quotation_status_id) REFERENCES public.business_catalog_item(id);


--
-- Name: repair_order fk_repair_order_appointment_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_appointment_business FOREIGN KEY (appointment_id, business_id) REFERENCES public.appointment(id, business_id);


--
-- Name: repair_order fk_repair_order_assignee_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_assignee_business FOREIGN KEY (assigned_business_user_id, business_id) REFERENCES public.business_user(id, business_id);


--
-- Name: repair_order fk_repair_order_branch_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_branch_business FOREIGN KEY (branch_id, business_id) REFERENCES public.business_branch(id, business_id);


--
-- Name: repair_order_commission fk_repair_order_commission_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_business FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: repair_order_commission fk_repair_order_commission_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_business_user FOREIGN KEY (business_user_id) REFERENCES public.business_user(id);


--
-- Name: repair_order_commission fk_repair_order_commission_order; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_order FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order_commission fk_repair_order_commission_settlement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_commission
    ADD CONSTRAINT fk_repair_order_commission_settlement FOREIGN KEY (settlement_id) REFERENCES public.commission_settlement(id);


--
-- Name: repair_order fk_repair_order_device_business_customer; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_device_business_customer FOREIGN KEY (customer_device_id, business_id, customer_id) REFERENCES public.customer_device(id, business_id, customer_id);


--
-- Name: business_review fk_repair_order_id_business_review; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_review
    ADD CONSTRAINT fk_repair_order_id_business_review FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order_attachment fk_repair_order_id_repair_order_attachment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_attachment
    ADD CONSTRAINT fk_repair_order_id_repair_order_attachment FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order_history fk_repair_order_id_repair_order_history; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_history
    ADD CONSTRAINT fk_repair_order_id_repair_order_history FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order_part fk_repair_order_id_repair_order_part; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_part
    ADD CONSTRAINT fk_repair_order_id_repair_order_part FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order_quality_check fk_repair_order_id_repair_order_quality_check; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_quality_check
    ADD CONSTRAINT fk_repair_order_id_repair_order_quality_check FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: stock_movement fk_repair_order_id_stock_movement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_repair_order_id_stock_movement FOREIGN KEY (repair_order_id) REFERENCES public.repair_order(id);


--
-- Name: repair_order fk_repair_order_receiver_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_receiver_business FOREIGN KEY (received_by_business_user_id, business_id) REFERENCES public.business_user(id, business_id);


--
-- Name: repair_order_sequence fk_repair_order_sequence_branch; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_sequence
    ADD CONSTRAINT fk_repair_order_sequence_branch FOREIGN KEY (branch_id) REFERENCES public.business_branch(id);


--
-- Name: repair_order_sequence fk_repair_order_sequence_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_sequence
    ADD CONSTRAINT fk_repair_order_sequence_business FOREIGN KEY (business_id) REFERENCES public.business(id);


--
-- Name: repair_order fk_repair_order_service_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_service_business FOREIGN KEY (business_service_id, business_id) REFERENCES public.business_service(id, business_id);


--
-- Name: repair_order fk_repair_order_status_id_repair_order; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order
    ADD CONSTRAINT fk_repair_order_status_id_repair_order FOREIGN KEY (status_id) REFERENCES public.business_catalog_item(id);


--
-- Name: repair_order_history fk_repair_order_status_id_repair_order_history; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.repair_order_history
    ADD CONSTRAINT fk_repair_order_status_id_repair_order_history FOREIGN KEY (status_id) REFERENCES public.business_catalog_item(id);


--
-- Name: business_user fk_role_id_business_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_user
    ADD CONSTRAINT fk_role_id_business_user FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: role_permission fk_role_permission_permission; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES public.permission(id);


--
-- Name: role_permission fk_role_permission_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: sale fk_sale_actor_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT fk_sale_actor_business FOREIGN KEY (sold_by_business_user_id, business_id) REFERENCES public.business_user(id, business_id);


--
-- Name: sale fk_sale_branch_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT fk_sale_branch_business FOREIGN KEY (branch_id, business_id) REFERENCES public.business_branch(id, business_id);


--
-- Name: sale_item fk_sale_id_sale_item; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sale_item
    ADD CONSTRAINT fk_sale_id_sale_item FOREIGN KEY (sale_id) REFERENCES public.sale(id);


--
-- Name: stock_movement fk_sale_item_id_stock_movement; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_sale_item_id_stock_movement FOREIGN KEY (sale_item_id) REFERENCES public.sale_item(id);


--
-- Name: business_schedule fk_schedule_branch_business; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_schedule
    ADD CONSTRAINT fk_schedule_branch_business FOREIGN KEY (branch_id, business_id) REFERENCES public.business_branch(id, business_id);


--
-- Name: business_service fk_service_category_id_business_service; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.business_service
    ADD CONSTRAINT fk_service_category_id_business_service FOREIGN KEY (service_category_id) REFERENCES public.business_catalog_item(id);


--
-- Name: stock_movement fk_stock_movement_inventory_branch; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fk_stock_movement_inventory_branch FOREIGN KEY (inventory_item_id, branch_id) REFERENCES public.inventory_item(id, branch_id);


--
-- Name: customer_device fk_user_id_customer_device; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customer_device
    ADD CONSTRAINT fk_user_id_customer_device FOREIGN KEY (customer_id) REFERENCES public.app_user(id);


--
-- Name: user_role fk_user_role_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: user_role fk_user_role_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- Name: refresh_token refresh_token_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_token
    ADD CONSTRAINT refresh_token_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.app_user(id);


--
-- PostgreSQL database dump complete
--

\unrestrict cfrvFsfsjWN4d4itvFYVdX6qnrNHt8wZ71c95uiXaW6vyAWfG9y3kiuNpkUd4jA

