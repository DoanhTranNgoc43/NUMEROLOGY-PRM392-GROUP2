using System.Net.Mail;

namespace Numerology.Core.Common;

public static class EmailHelper
{
    public static string GetUsername(string email)
    {
        var address = new MailAddress(email);
        return address.User;
    }

    public static bool IsMail(string email)
    {
        return !string.IsNullOrEmpty(email) && email.Contains('@');
    }
}