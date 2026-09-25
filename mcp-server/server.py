"""
Minimal MCP server skeleton for the SME Compliance Assistant.

Exposes tools that Copilot (or any MCP client) can call:
  - validate_vat_number
  - check_invoice_format
  - suggest_account_category

This is intentionally a skeleton — fill in the real logic as part
of the roadmap's MCP stage. Requires: pip install mcp
"""

from mcp.server.fastmcp import FastMCP

mcp = FastMCP("sme-compliance")


@mcp.tool()
def validate_vat_number(vat_number: str, country_code: str) -> dict:
    """
    Validate an EU VAT number against the VIES service.

    Args:
        vat_number: The VAT number without country prefix, e.g. "0123456789"
        country_code: Two-letter ISO country code, e.g. "BE"

    Returns:
        dict with keys: valid (bool), company_name (str | None), raw_response (str | None)
    """
    # TODO: call the VIES SOAP/REST endpoint and parse the response
    raise NotImplementedError


@mcp.tool()
def check_invoice_format(invoice_number: str) -> dict:
    """
    Check whether an invoice number follows Belgian sequential
    numbering conventions.

    Args:
        invoice_number: The invoice number to check

    Returns:
        dict with keys: valid (bool), reason (str | None)
    """
    # TODO: implement the actual Belgian numbering rule check
    raise NotImplementedError


@mcp.tool()
def suggest_account_category(description: str) -> dict:
    """
    Suggest a MAR chart-of-accounts category for a transaction
    description.

    Args:
        description: Free-text transaction description

    Returns:
        dict with keys: suggested_code (str), suggested_label (str), confidence (float)
    """
    # TODO: implement matching logic (rules first, ML/LLM later)
    raise NotImplementedError


if __name__ == "__main__":
    mcp.run()
